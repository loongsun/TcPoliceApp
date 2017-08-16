package loongsun.com.facetest.reconova;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.io.File;
import loongsun.com.facetest.reconova.api.request.person.PersonMatchRequest;
import loongsun.com.facetest.reconova.api.request.person.PersonRegisterRequest;
import loongsun.com.facetest.reconova.api.request.tool.DetectFaceInFileRequest;
import loongsun.com.facetest.reconova.api.request.tool.DetectFaceRequest;
import loongsun.com.facetest.reconova.api.request.tool.FeatureMatchRequest;
import loongsun.com.facetest.reconova.api.request.tool.ImageMatchRequest;
import loongsun.com.facetest.reconova.api.response.FcsApiResponse;
import loongsun.com.facetest.reconova.api.response.model.FaceDetectResult;
import loongsun.com.facetest.reconova.client.FcsApiClient;
import loongsun.com.facetest.reconova.util.FileUtils;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		//FcsApiClient client = setupClient();
		
		//String feature1 = testDetectFaceInFile(client, "o:/1.jpg");
		//String feature2 = testDetectFaceInFile(client, "o:/2.jpg");
		
		//Double similarity = testFeatureMatch(client, feature1, feature2);
	}
	
	public static FcsApiClient setupClient(String ip,int port,String name,String psw )
	{
//		boolean isAut=false;
//		if(name==null||psw==null||"".equals(name)||"".equals(psw))
//		{
//			isAut=false;
//		}
//		else
//		{
//			isAut=true;
//		}
		FcsApiClient client = FcsApiClient.createFcsApiClient(true);
		client.setProtocol("http");
		client.setHost(ip);
		client.setPort(port);
		client.setUsername(name);
		client.setPassword(psw);
		return client;
	}

	public static JSONObject testDetectFace(FcsApiClient client, String path) {
		String base64Image = FileUtils.readFileAsBase64String(path);
		DetectFaceRequest detectFaceRequest = new DetectFaceRequest(client);
		detectFaceRequest.setImageBase64Data(base64Image);
		FcsApiResponse resp = detectFaceRequest.execute();
		JSONObject data = resp.getData();
		return data;
	}




	
	public static Double testFeatureMatch(FcsApiClient client, String feature1, String feature2) {
		FeatureMatchRequest featureMatchRequest = new FeatureMatchRequest(client);
		featureMatchRequest.setFeature1(feature1);
		featureMatchRequest.setFeature2(feature2);
		
		FcsApiResponse resp = featureMatchRequest.execute();
		
		
		if (resp.getErrorCode() == 0) {
			JSONObject data = resp.getData();
			Double similarity = data.getDouble("similarity");
			System.out.println(similarity);
			return similarity;
		} else {
			return 0d;
		}
		
	}

	public static Double testImageMatch(FcsApiClient client,String path1,String path2) {
		String image1 = FileUtils.readFileAsBase64String(path1);
		String image2 = FileUtils.readFileAsBase64String(path2);

		ImageMatchRequest imageMatchRequest = new ImageMatchRequest(client, image1, image2);

		FcsApiResponse resp = imageMatchRequest.execute();
		return getMatchSimilarity(resp);

	}
	/**
	 * 测试注册单个人员
	 *
	 * @param client
	 * @throws ParseException
	 */
	public static String testPersonRegister(FcsApiClient client,String picPath,Long dbId,String name,int sex) throws ParseException {
		//准备参数
		String imageData = FileUtils.readFileAsBase64String(picPath);
		System.out.println(imageData);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date validTo = sdf.parse("2099-12-30");

		PersonRegisterRequest personRegisterRequest = new PersonRegisterRequest(client);

		personRegisterRequest.setDbId(dbId);
		personRegisterRequest.setGender(sex);
		personRegisterRequest.setImageData(imageData);
		personRegisterRequest.setName(name);
		personRegisterRequest.setValidTo(validTo);

		FcsApiResponse resp = personRegisterRequest.execute();
		return resp.toString();

	}
	public static JSONObject  testPersonMatch(FcsApiClient client,String picPath,String dbId) {

		List<String> dbIds = Arrays.asList(dbId);
		FcsApiResponse detectResp = testDetectFaceInFile1(client,picPath);
		String feature = getDetectFeatureString(detectResp);
		PersonMatchRequest personMatchRequest = new PersonMatchRequest(client);
		personMatchRequest.setDbIds(dbIds);
		personMatchRequest.setFeature(feature);
		personMatchRequest.setSimilarity(80d);//记得改回来
		FcsApiResponse resp = personMatchRequest.execute();
		return resp.getData();
		//System.out.println(resp.getData().toJSONString());
	}

	public static String  testPersonMatchNew(FcsApiClient client,String feature,String dbId) {

		List<String> dbIds = Arrays.asList(dbId);
//		FcsApiResponse detectResp = testDetectFaceInFile1(client,picPath);
//		String feature = getDetectFeatureString(detectResp);
		PersonMatchRequest personMatchRequest = new PersonMatchRequest(client);
		personMatchRequest.setDbIds(dbIds);
		personMatchRequest.setFeature(feature);
		personMatchRequest.setSimilarity(10d);
		FcsApiResponse resp = personMatchRequest.execute();
		return resp.toString();
		//System.out.println(resp.getData().toJSONString());
	}





	public static FcsApiResponse testDetectFaceInFile1(FcsApiClient client, String path) {
		DetectFaceInFileRequest detectFaceInFileRequest = new DetectFaceInFileRequest(client);
		detectFaceInFileRequest.setFile(new File(path));
		FcsApiResponse resp = detectFaceInFileRequest.execute();
		return resp;
	}

	/**
	 * 解析返回的比对相似度
	 *
	 * @param resp
	 * @return
	 */
	private static Double getMatchSimilarity(FcsApiResponse resp) {
		if (resp.getErrorCode() == 0) {
			JSONObject data = resp.getData();
			Double similarity = data.getDouble("similarity");
			System.out.println(similarity);
			return similarity;
		} else {
			return 0d;
		}
	}
	/**
	 * 从返回值中获取特征
	 *
	 * @param resp
	 * @return
	 */
	private static String getDetectImageString(FcsApiResponse resp) {
		JSONArray result = resp.getData().getJSONArray("result");

		if (resp.getErrorCode() == 0 && result.size() > 0) {
			JSONObject first = result.getJSONObject(0);
			System.out.println(first.toJSONString());
			FaceDetectResult detectResult = JSON.parseObject(first.toJSONString(), FaceDetectResult.class);

			return detectResult.getFaceImage();
		} else {
			return "";
		}
	}


	/**
	 * 从返回值中获取特征
	 *
	 * @param resp
	 * @return
	 */
	private static String getDetectFeatureString(FcsApiResponse resp) {
		JSONArray result = resp.getData().getJSONArray("result");
		if (resp.getErrorCode() == 0 && result.size() > 0) {
			JSONObject first = result.getJSONObject(0);
			FaceDetectResult detectResult = JSON.parseObject(first.toJSONString(), FaceDetectResult.class);
			return detectResult.getFeature();
		} else {
			return "";
		}
	}
}

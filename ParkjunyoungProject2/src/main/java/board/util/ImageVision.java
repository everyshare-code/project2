package board.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.LocalizedObjectAnnotation;
import com.google.protobuf.ByteString;

public class ImageVision {
	
	

	public static String detectLocalizedObjects(String filePath) throws IOException {
		  List<AnnotateImageRequest> requests = new ArrayList<>();
		  StringBuffer buffer=new StringBuffer();
		  com.google.protobuf.ByteString imgBytes = com.google.protobuf.ByteString.copyFrom(java.util.Base64.getDecoder().decode(filePath.split(",")[1]));

		  Image img = Image.newBuilder().setContent(imgBytes).build();
		  AnnotateImageRequest request =
		      AnnotateImageRequest.newBuilder()
		          .addFeatures(Feature.newBuilder().setType(Type.OBJECT_LOCALIZATION))
		          .setImage(img)
		          .build();
		  requests.add(request);

		  // Initialize client that will be used to send requests. This client only needs to be created
		  // once, and can be reused for multiple requests. After completing all of your requests, call
		  // the "close" method on the client to safely clean up any remaining background resources.
		  try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
		    // Perform the request
		    BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
		    List<AnnotateImageResponse> responses = response.getResponsesList();

		    // Display the results
		    for (AnnotateImageResponse res : responses) {
		      for (LocalizedObjectAnnotation entity : res.getLocalizedObjectAnnotationsList()) {
		    	  buffer.append("#"+entity.getName());
		    	  System.out.format("Object name: %s%n", entity.getName());
		    	  entity
		            .getBoundingPoly()
		            .getNormalizedVerticesList()
		            .forEach(vertex -> System.out.format("- (%s, %s)%n", vertex.getX(), vertex.getY()));
		      }
		    }
		  }
		  return buffer.toString();
	}
	
	public static String detectText(String filePath) throws IOException {
		List<AnnotateImageRequest> requests = new ArrayList<>();
		StringBuffer buffer=new StringBuffer();

		ByteString imgBytes = ByteString.copyFrom(java.util.Base64.getDecoder().decode(filePath.split(",")[1]));

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
		AnnotateImageRequest request =
			AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);

		// Initialize client that will be used to send requests. This client only needs to be created
		// once, and can be reused for multiple requests. After completing all of your requests, call
		// the "close" method on the client to safely clean up any remaining background resources.
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
		  BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
		  List<AnnotateImageResponse> responses = response.getResponsesList();

		  for (AnnotateImageResponse res : responses) {
			if (res.hasError()) {
			  System.out.format("Error: %s%n", res.getError().getMessage());
			  return null;
			}
			
			int i=0;
			// For full list of available annotations, see http://g.co/cloud/vision/docs
			for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
				if(++i>1)buffer.append("#"+annotation.getDescription());
			}
		  }
		  return buffer.toString();
		}
	  }
	
	
}

package board.util;

import java.io.IOException;

import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;

public class TranslateText {
	// Set and pass variables to overloaded translateText() method for translation.
	  public static String translateText(String text) throws IOException {
	    // TODO(developer): Replace these variables before running the sample.
	    String projectId = BoardUtil.getUser("projectId");
	    // Supported Languages: https://cloud.google.com/translate/docs/languages
	    String targetLanguage = "ko";
	    
	    return translateText(projectId, targetLanguage, text);
	  }

	  // Translate text to target language.
	  public static String translateText(String projectId, String targetLanguage, String text)
	      throws IOException {
		  StringBuffer buffer=new StringBuffer();
	    // Initialize client that will be used to send requests. This client only needs to be created
	    // once, and can be reused for multiple requests. After completing all of your requests, call
	    // the "close" method on the client to safely clean up any remaining background resources.
	    try (TranslationServiceClient client = TranslationServiceClient.create()) {
	      // Supported Locations: `global`, [glossary location], or [model location]
	      // Glossaries must be hosted in `us-central1`
	      // Custom Models must use the same location as your model. (us-central1)
	      LocationName parent = LocationName.of(projectId, "global");

	      // Supported Mime Types: https://cloud.google.com/translate/docs/supported-formats
	      TranslateTextRequest request =
	          TranslateTextRequest.newBuilder()
	              .setParent(parent.toString())
	              .setMimeType("text/plain")
	              .setTargetLanguageCode(targetLanguage)
	              .addContents(text)
	              .build();

	      TranslateTextResponse response = client.translateText(request);
	      int i=0;
	      // Display the translation for each input text provided
	      for (Translation translation : response.getTranslationsList()) {
//	        System.out.printf("Translated text: %s\n", translation.getTranslatedText());
	    	  System.out.println(++i+"번쨰:"+translation.getTranslatedText());
	        buffer.append(translation.getTranslatedText());
	      }
	    }
	    return buffer.toString();
	  }
}

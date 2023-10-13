package cmc.mellyserver.apidocs;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

public class RestDocsUtils {

    public static OperationRequestPreprocessor requestPreprocessor() {
        return Preprocessors.preprocessRequest(Preprocessors.prettyPrint());
    }

    public static OperationResponsePreprocessor responsePreprocessor() {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint());
    }

}

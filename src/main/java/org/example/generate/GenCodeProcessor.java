package org.example.generate;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class GenCodeProcessor extends AbstractProcessor {
    private ProcessingEnvironment processingEnvironment;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        new GenConvertMapping(roundEnv, processingEnvironment).generate();
        return false;
    }
}

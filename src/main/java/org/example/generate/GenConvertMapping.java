package org.example.generate;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import org.example.antation.MappingObject;
import org.springframework.stereotype.Component;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import java.io.IOException;

public class GenConvertMapping {
    private RoundEnvironment roundEnv;

    private ProcessingEnvironment processingEnvironment;

    public GenConvertMapping(RoundEnvironment roundEnv, ProcessingEnvironment processingEnvironment) {
        this.roundEnv = roundEnv;
        this.processingEnvironment = processingEnvironment;
    }

    public void generate() {
        //duyệt qua toàn bộ các lớp có anotation là DAO để thực hiện gen class impl
        System.out.println("Processing annotations...");
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(MappingObject.class)) {
            try {
                createClass(annotatedElement);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createClass(Element abstractElement) throws IOException {
        Element enclosingElement = abstractElement.getEnclosingElement();
        while (!(enclosingElement instanceof PackageElement)) {
            enclosingElement = enclosingElement.getEnclosingElement();
        }
        PackageElement packageElement = (PackageElement) enclosingElement;
        String packageName = packageElement.getQualifiedName().toString();
        String className = abstractElement.getSimpleName().toString();
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);
        System.out.println(className);

        var builder = TypeSpec.classBuilder(className + "Impl")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Component.class);
        createSave(builder, abstractElement);
        var typeSpec = builder.build();
        var javaFile = JavaFile.builder(packageName, typeSpec).build();
        javaFile.writeTo(processingEnvironment.getFiler());
    }

    private void createSave(TypeSpec.Builder builder, Element annotatedElement) {
        MethodSpec getSort = MethodSpec.methodBuilder("save")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("System.out.println(\"Thành công\")")
                .build();
        builder.addMethod(getSort);
    }
}

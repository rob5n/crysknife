package org.treblereel.gwt.crysknife.generator;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.lang.model.element.TypeElement;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import org.treblereel.gwt.crysknife.annotation.Generator;
import org.treblereel.gwt.crysknife.client.Application;
import org.treblereel.gwt.crysknife.generator.api.ClassBuilder;
import org.treblereel.gwt.crysknife.generator.context.GenerationContext;
import org.treblereel.gwt.crysknife.generator.context.IOCContext;
import org.treblereel.gwt.crysknife.generator.definition.BeanDefinition;
import org.treblereel.gwt.crysknife.generator.definition.Definition;
import org.treblereel.gwt.crysknife.util.Utils;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 4/5/19
 */
@Generator(priority = 100000)
public class BootstrapperGenerator extends ScopedBeanGenerator {

    private String BOOTSTRAP_EXTENSION = "Bootstrap";

    @Override
    public void register(IOCContext iocContext) {
        iocContext.register(Application.class, WiringElementType.DEPENDENT_BEAN, this);
        this.iocContext = iocContext;
    }

    @Override
    public void generate(ClassBuilder clazz, Definition definition) {
        super.generate(clazz, definition);
    }

    @Override
    public void generateDependantFieldDeclaration(ClassBuilder classBuilder, BeanDefinition beanDefinition) {
        classBuilder.addConstructorDeclaration(Modifier.Keyword.PACKAGE_PRIVATE);
        Parameter arg = new Parameter();
        arg.setName("application");
        arg.setType(beanDefinition.getType().getSimpleName().toString());

        classBuilder.getConstructorDeclaration().getParameters().add(arg);
        beanDefinition.getDependsOn().forEach(on -> generateFactoryFieldDeclaration(classBuilder, on));

        AssignExpr assign = new AssignExpr().setTarget(new FieldAccessExpr(new ThisExpr(), "instance")).setValue(new NameExpr("application"));
        classBuilder.getConstructorDeclaration().getBody().addAndGetStatement(assign);
    }

    @Override
    public void initClassBuilder(ClassBuilder clazz, BeanDefinition beanDefinition) {
        clazz.getClassCompilationUnit().setPackageDeclaration(beanDefinition.getPackageName());
        clazz.getClassCompilationUnit().addImport("org.treblereel.gwt.crysknife.client.internal.Factory");
        clazz.getClassCompilationUnit().addImport(Provider.class);
        clazz.getClassCompilationUnit().addImport(beanDefinition.getQualifiedName());
        clazz.setClassName(beanDefinition.getType().getSimpleName().toString() + BOOTSTRAP_EXTENSION);

        clazz.getClassDeclaration().addField(beanDefinition.getClassName(), "instance", Modifier.Keyword.PRIVATE);
    }

    @Override
    public void generateInstanceGetMethodBuilder(ClassBuilder classBuilder, BeanDefinition beanDefinition) {
        MethodDeclaration getMethodDeclaration = classBuilder.getClassDeclaration()
                .addMethod("initialize");
        classBuilder.setGetMethodDeclaration(getMethodDeclaration);
    }

    @Override
    public void generateInstanceGetMethodReturn(ClassBuilder classBuilder, BeanDefinition beanDefinition) {

    }

    @Override
    public void generateFactoryCreateMethod(ClassBuilder classBuilder, BeanDefinition beanDefinition) {

    }

    @Override
    public void write(ClassBuilder clazz, BeanDefinition beanDefinition, GenerationContext context) {
        try {
            String fileName = Utils.getQualifiedName(beanDefinition.getType()) + BOOTSTRAP_EXTENSION;
            String source = clazz.toSourceCode();
            build(fileName, source, context);
        } catch (IOException e1) {
            throw new Error(e1);
        }
    }
}
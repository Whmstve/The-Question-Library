package net.whmstve.thequestionlib.general.content.annotations.processors;

import net.whmstve.thequestionlib.general.content.annotations.OnlyImplementedBy;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;


@SupportedAnnotationTypes("net.whmstve.thequestionlib.general.content.annotations.OnlyImplementedBy")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class OnlyImplementedByProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.typeUtils = (Types) processingEnv.getTypeUtils();
        this.elementUtils = processingEnv.getElementUtils();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(OnlyImplementedBy.class)) {

            // 1. Must be an interface
            if (element.getKind() != ElementKind.INTERFACE) {
                error(element, "@AllowedForBaseClass can only be applied to interfaces");
                continue;
            }

            // 2. Get the base class type from annotation
            OnlyImplementedBy annotation = element.getAnnotation(OnlyImplementedBy.class);
            TypeElement baseClassElem = elementUtils.getTypeElement(annotation.clazz().getCanonicalName());
            if (baseClassElem == null) {
                error(element, "Base class %s not found", annotation.clazz().getCanonicalName());
                continue;
            }
            TypeMirror baseType = baseClassElem.asType();

            // 3. Check all known classes in this compilation round
            for (Element rootElement : roundEnv.getRootElements()) {
                if (rootElement.getKind() == ElementKind.CLASS) {
                    TypeElement classElem = (TypeElement) rootElement;

                    // Does this class implement the annotated interface?
                    if (implementsInterface(classElem.asType(), (TypeElement) element)) {
                        // If so, check if it's assignable to the base class
                        if (!typeUtils.isAssignable(classElem.asType(), baseType)) {
                            error(classElem,
                                    "Class %s implements %s but is not a subclass of %s",
                                    classElem.getQualifiedName(),
                                    element.getSimpleName(),
                                    baseClassElem.getQualifiedName()
                            );
                        }
                    }
                }
            }
        }
        return true; // We handled this annotation
    }

    // Utility: check if a type implements a given interface
    private boolean implementsInterface(TypeMirror type, TypeElement iface) {
        for (TypeMirror ifaceType : typeUtils.directSupertypes(type)) {
            Element e = typeUtils.asElement(ifaceType);
            if (e instanceof TypeElement te) {
                if (te.equals(iface)) {
                    return true;
                }
                // Recursive search up the tree
                if (implementsInterface(te.asType(), iface)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Utility: report error
    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
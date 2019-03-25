package org.treblereel.client;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLLabelElement;
import org.treblereel.client.inject.BeanOne;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 2/22/19
 */
@Singleton
public class SingletonBeans {

    @Inject
    HTMLDivElement form;

    @Inject
    HTMLDivElement formGroup;

    @Inject
    HTMLLabelElement formLabel;

    @Inject
    HTMLInputElement textBox;

    @Inject
    HTMLButtonElement checkBtn;

    @Inject
    BeanOne beanOne1Instance;

    @Inject
    BeanOne beanOne2Instance;

    @PostConstruct
    public void init() {
        formGroup.className = "form-group";

        formLabel.textContent = "@Singleton test, the same instance in two fields, random number assigned on construction";
        formLabel.setAttribute("setFor", "SingletonBeans");
        formLabel.className = "control-label";

        textBox.id = "SingletonBeans";
        textBox.disabled = true;
        textBox.className = "form-control";
        textBox.style.width = CSSProperties.WidthUnionType.of("300px");

        formGroup.appendChild(formLabel);
        formGroup.appendChild(textBox);

        form.appendChild(formGroup);

        initBtn();
    }

    private void initBtn() {
        checkBtn.textContent = "Check";
        checkBtn.className = "btn btn-default";
        checkBtn.addEventListener("click", evt -> {
            StringBuffer sb = new StringBuffer();
            sb.append("beanOne1Instance random :");
            sb.append(beanOne1Instance.getRandom());
            sb.append(", beanOne2Instance random :");
            sb.append(beanOne2Instance.getRandom());
            sb.append(", ? equal " + (beanOne1Instance.getRandom() == beanOne2Instance.getRandom()));

            setText(sb.toString());
        });

        formGroup.appendChild(checkBtn);
    }

    private void setText(String text) {
        textBox.value = text;
    }

    public HTMLElement asElement() {
        return form;
    }
}
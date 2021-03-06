package org.treblereel.client.inject.named;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author Dmitrii Tikhomirov
 * Created by treblereel 2/21/19
 */
@Named("bird")
@Singleton
public class Bird implements Animal {

    @Override
    public String say() {
        return "tweet";
    }
}


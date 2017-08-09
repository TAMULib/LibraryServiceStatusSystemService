package edu.tamu.app.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import edu.tamu.app.model.AppUser;
import edu.tamu.framework.aspect.CoreControllerAspect;

/**
 * Application Controller Aspect
 * 
 */
@Component
@Aspect
public class AppControllerAspect extends CoreControllerAspect<AppUser> {

}
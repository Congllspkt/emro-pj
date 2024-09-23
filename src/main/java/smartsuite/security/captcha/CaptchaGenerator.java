package smartsuite.security.captcha;

import org.springframework.beans.factory.InitializingBean;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.BackgroundProducer;
import cn.apiclub.captcha.backgrounds.TransparentBackgroundProducer;
import cn.apiclub.captcha.gimpy.GimpyRenderer;
import cn.apiclub.captcha.gimpy.RippleGimpyRenderer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import cn.apiclub.captcha.text.renderer.WordRenderer;

/*
Captcha 생성 API
http://simplecaptcha.sourceforge.net/custom_images.html
*/

public class CaptchaGenerator implements InitializingBean {

	private BackgroundProducer backgroundProducer;
	private TextProducer textProducer;
	private WordRenderer wordRenderer;
	private GimpyRenderer gimpyRenderer;

	public Captcha createCaptcha(int width, int height) {
		Captcha.Builder builder = new Captcha.Builder(width, height)
				.addBackground(backgroundProducer)
				.addText(textProducer, wordRenderer)
				.gimp(gimpyRenderer)
				.addNoise()
				.addNoise();
		
		return builder.build();
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.backgroundProducer == null) {
			this.backgroundProducer = new TransparentBackgroundProducer();
		}

		if (this.textProducer == null) {
			this.textProducer = new DefaultTextProducer();
		}

		if (this.wordRenderer == null) {
			this.wordRenderer = new DefaultWordRenderer();
		}

		if (this.gimpyRenderer == null) {
			this.gimpyRenderer = new RippleGimpyRenderer();
		}
	}
}
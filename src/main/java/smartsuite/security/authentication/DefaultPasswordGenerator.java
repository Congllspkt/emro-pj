package smartsuite.security.authentication;

import java.util.ArrayList;
import java.util.List;

import edu.vt.middleware.password.CharacterRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LowercaseCharacterRule;
import edu.vt.middleware.password.UppercaseCharacterRule;

public class DefaultPasswordGenerator implements PasswordGenerator {

	int length = 20;
	
	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String generate() {
		edu.vt.middleware.password.PasswordGenerator generator = new edu.vt.middleware.password.PasswordGenerator();
		List<CharacterRule> rules = new ArrayList<CharacterRule>();
		rules.add(new DigitCharacterRule(1));
		rules.add(new UppercaseCharacterRule(1));
		rules.add(new LowercaseCharacterRule(1));
		return generator.generatePassword(length, rules);
	}
	
}

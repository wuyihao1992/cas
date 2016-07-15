package org.jasig.cas.support.pms;

import org.junit.Test;

public class PmsAuthenticationHandlerTests {

	@Test
	public void test(){

	}

	@Test
	public void testPassword(){
		String result = "0bc8b6dd5bea7eff330bb23687b77b0e";
		String code = PmsPasswordEncoder.encode("hengte@2016", "ic1yrtistpmu");
		System.out.print(String.format("result : %s, code : %s , boolean : %s", result, code, result.equals(code)));
	}
}



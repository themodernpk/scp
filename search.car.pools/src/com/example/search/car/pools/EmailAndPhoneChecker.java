package com.example.search.car.pools;

import java.util.regex.Pattern;

import android.util.Patterns;

public class EmailAndPhoneChecker {

	public boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
	
	public boolean validPhone(String phone) {
	    Pattern pattern = Patterns.PHONE;
	    return pattern.matcher(phone).matches();
	}
	
}

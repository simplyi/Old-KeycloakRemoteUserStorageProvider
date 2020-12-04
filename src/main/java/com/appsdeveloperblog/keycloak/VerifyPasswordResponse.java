package com.appsdeveloperblog.keycloak;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author sergeykargopolov
 */
public class VerifyPasswordResponse {
    private boolean result;
    
    public VerifyPasswordResponse() {}
 
    public VerifyPasswordResponse(boolean result) {
		this.result = result;
	}

	public boolean getResult() {
        return result;
    }
 
    public void setResult(boolean result) {
        this.result = result;
    }
 
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.adridi.generalmanagement.gmgateway.security;

/**
 *
 * Save created JWT token for authentication of users
 * 
 * @author A.Dridi
 */
public class JwtProperties {
    public static final String SECRET = "generalm529232";
    //In milliseconds. 10 hours
    public static final long EXPIRATION_TIME = 36000000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String HEADER_USERID_STRING="ETag";
}

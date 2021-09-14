
package pss1.domain;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;

public enum Profile {

    ADMIN("ROLE_USER","ROLE_ADMIN"),
    
    USER("ROLE_USER");
    
    private final GrantedAuthority[] authorities;
    
    private Profile(String... roleNames){
        authorities = new GrantedAuthority[roleNames.length];
        for (int i = 0; i < authorities.length; i++){
            authorities[i] = new GrantedAuthorityImpl(roleNames[i]);
        }
    }
    
    public GrantedAuthority[] getAuthorities() {
        return authorities;
    }
}
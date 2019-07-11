package io.riggo.web;


import java.util.Map;

import org.springframework.test.context.junit4.SpringRunner;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
public class JWTDecodeContextInfoTest {

    @Test
    public void shouldGetSubject() throws Exception {
        DecodedJWT jwt = JWT.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ");
        assertThat(jwt.getSubject(), is(notNullValue()));
        assertThat(jwt.getSubject(), is("1234567890"));
    }

    @Test
    public void shouldDecodeInfoWhenAuth0InfoPayload() throws Exception {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik9EQkZSa0pFTmprelJqTXdOVU0xTVRSRVF6WXpORU0wTjBFM1FUSkJNVGd3UkRjeE9UQTJOdyJ9.eyJpc3MiOiJodHRwczovL3JpZ2dvLXFhLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw1ZDE2NzYzYzFjOGQzZDBjYjg5MzFlNTYiLCJhdWQiOlsibG9hZC1yZXNvdXJjZS1hcGkiLCJodHRwczovL3JpZ2dvLXFhLmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE1NjI3NTg1MjUsImV4cCI6MTU2Mjc2NTcyNSwiYXpwIjoiN25BWTRHVkpHQlhRUWgwdXUzVGY5YTFZU1B1NVR3dXYiLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIHJlYWQ6bG9hZCIsInBlcm1pc3Npb25zIjpbInJlYWQ6bG9hZCIsIndyaXRlOmxvYWQiXX0.CAGLdTlRGEuPR8-ZTcdyz9uZygfcsIE-pz46uBuCjY9LvTgpqdIwE3SuYHFPlq8G2w8OiwPe0WlWurYokTFlDy4HVtkO3099YYds2CT9y_zTWAImc8tDnM0Gm1xR06b-N4W5c4b7S_8CbLRRqVhOkxDyB8PjEuO2Wcv6F77JHfxT7dd6NdXH9NHQoBv3-1ni2i0zBBFWEPgsqbKjmoE_wVK2AqHgP87ozG2JlftQjbmYVq2ym5fCz-ZNSki3dfDRBhPI1MnvnykboNO4XsMg64H4gmaKZYr2pgJLcX7n02VcrQXvyMbVcebBYNFDDummlBENIP9c57o8EOKm4571_A");
        assertThat(jwt, is(notNullValue()));
        assertThat(jwt.getClaims(), is(notNullValue()));
        assertThat(jwt.getClaims(), is(instanceOf(Map.class)));
        assertThat(jwt.getClaims().get("iss"), is(notNullValue()));
        assertThat(jwt.getClaims().get("sub"), is(notNullValue()));
        assertThat(jwt.getClaims().get("aud"), is(notNullValue()));
        assertThat(jwt.getClaims().get("iat"), is(notNullValue()));
        assertThat(jwt.getClaims().get("exp"), is(notNullValue()));
        assertThat(jwt.getClaims().get("azp"), is(notNullValue()));
        assertThat(jwt.getClaims().get("scope"), is(notNullValue()));
        assertThat(jwt.getClaims().get("permissions"), is(notNullValue()));
    }
    @Test
    public void shouldGetCustomInfoAuth0WhenPayload() throws Exception {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik9EQkZSa0pFTmprelJqTXdOVU0xTVRSRVF6WXpORU0wTjBFM1FUSkJNVGd3UkRjeE9UQTJOdyJ9.eyJodHRwczovL2F1dGgucmlnZ29xYS5jb20vc2l0ZUlkIjoiMTAwIiwiaHR0cHM6Ly9hdXRoLnJpZ2dvcWEuY29tL2VtYWlsIjoiaXNtYWVsQHJpZ2dvLmlvIiwiaHR0cHM6Ly9hdXRoLnJpZ2dvcWEuY29tL2VtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiaXNzIjoiaHR0cHM6Ly9yaWdnby1xYS5hdXRoMC5jb20vIiwic3ViIjoiYXV0aDB8NWQxNjc2M2MxYzhkM2QwY2I4OTMxZTU2IiwiYXVkIjpbImxvYWQtcmVzb3VyY2UtYXBpIiwiaHR0cHM6Ly9yaWdnby1xYS5hdXRoMC5jb20vdXNlcmluZm8iXSwiaWF0IjoxNTYyNzYxMzE0LCJleHAiOjE1NjI3Njg1MTQsImF6cCI6IjduQVk0R1ZKR0JYUVFoMHV1M1RmOWExWVNQdTVUd3V2Iiwic2NvcGUiOiJvcGVuaWQgcHJvZmlsZSByZWFkOmxvYWQiLCJwZXJtaXNzaW9ucyI6WyJyZWFkOmxvYWQiLCJ3cml0ZTpsb2FkIl19.IYyfAQz2LwHgO972ZAYZsTilqGPnD8i2tpcnYoRvFNtI8rAKp3c_uFhEg40YB0ZSi5icBq2_bXBo2N3fbyfABUJ-Wxb8fkNoo_vGSVqjyoytxCGSaZij-1ftxhV36wPyJieGKlXqBnDcVCeA7fzbCTvpd9QTa0uD3TqS7T0WpafA5E0sM139C0dxKYS6Pap63xH5UGykF8n6nDOMR4JPezevBZZdPwZiIWtsyonmdmCJGlqfGXmziAB5cKYNIgc1xG2MOBBVzzIAL3KZvx8JyMCz8HPIn9dabYnbdj9osZcPZ0ACQr13TVLedetlt9MhESCPkUENgdOZ2v_hA7JivQ");
        assertThat(jwt, is(notNullValue()));
        assertThat(jwt.getClaims(), is(instanceOf(Map.class)));
        assertThat(jwt.getClaims().get("https://auth.riggoqa.com/siteId").asString(), is("100"));
        assertThat(jwt.getClaims().get("https://auth.riggoqa.com/email").asString(), is("ismael@riggo.io"));
        assertThat(jwt.getClaims().get("https://auth.riggoqa.com/email_verified").asBoolean(), is(false));
    }

    @Test
    public void shouldGetCustomClaimOfTypeBoolean() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjp0cnVlfQ.FwQ8VfsZNRqBa9PXMinSIQplfLU4-rkCLfIlTLg_MV0";
        DecodedJWT jwt = JWT.decode(token);
        assertThat(jwt, is(notNullValue()));
        assertThat(jwt.getClaim("name").asBoolean(), is(true));
    }


}

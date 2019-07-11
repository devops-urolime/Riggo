package io.riggo;

import org.springframework.boot.test.web.client.TestRestTemplate;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// TODO: Remove this controller if this is not used.
public class RestTests {

    //@LocalServerPort
    private int apiPort=8088;

    //@Autowired
    private TestRestTemplate restTemplate;

    //@Test
    public void restTest() throws Exception {
/*
        String accessToken  ="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IlFqSXlSa1k1TWtNNU5qVXpRemt6UkRreVEwRkVRa0pEUWtReE5UVkdSRFpGT1VFMVFqTXpRZyJ9.eyJpc3MiOiJodHRwczovL3JpZ2dvLmF1dGgwLmNvbS8iLCJzdWIiOiJTSDNwOTE1TW5oQUZ0SkYxQnpCYml6dTkzVFk5ekc4dkBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9jb2ZmZWUtYXV0aDAtYXBpLmhlcm9rdWFwcC5jb20iLCJpYXQiOjE1NTk4NDY2MjcsImV4cCI6MTU1OTkzMzAyNywiYXpwIjoiU0gzcDkxNU1uaEFGdEpGMUJ6QmJpenU5M1RZOXpHOHYiLCJzY29wZSI6InJlYWQ6Y29mZmVlIHJlYWQ6bWVzc2FnZXMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.JiVI3YbsdhUTnfJHsF4LKSrFYrn77_U1uVaEpqk4uQg7gLb_s_PUP2yrU_quAfFHc2eoT2uNrA0VJpQGbfnyajUjmRQyYTPmcd1dbj9cjuxH6Jx5fmvzsnSZAMevXBnh35GnBz75d5YJqLDjwO20wgGKH3Y5rSL0VkPpvdKWJCoEuUq3tCT0fm7lXkLVlG_Hjz66m19KKzIgKl9UIE0OzMs5g0PI3Ee8785ydY0GXuANeEA9OnDl6yHSGKGKQmEvxSQKNUpEJVblD-3GTjbCVbxstotzun16LCfMABjQzdLKDE8sncHg7R-FBZOmz0NNGzZGhh4yDS6EF6zEVUA93g";
       
        

        
    restTemplate.getRestTemplate().setInterceptors(Collections.singletonList((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);    
        
            return execution.execute(request, body);
        }));
        String s= restTemplate.getForObject("http://localhost:" + apiPort + "/api/v1/load/1", String.class);
        System.out.println(s);
        assertThat(s)
                .contains("\"id\":");
                */
    }
}
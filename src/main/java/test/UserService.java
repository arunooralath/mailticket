package test;

import pojo.Issue;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {

	@POST("issue/")
	Call<Issue> createIssue(@Header("Authorization") String credentials, @Body Issue issue);

}

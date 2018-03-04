package test;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

	private static final String BASE_URL = "https://issues.ngptools.com/jira/rest/api/latest/";

	final static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(180, TimeUnit.SECONDS)
			.writeTimeout(180, TimeUnit.SECONDS).readTimeout(180, TimeUnit.SECONDS).build();

	private static Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create());

	private static Retrofit retrofit = builder.build();

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	public static <S> S createService(Class<S> serviceClass) {
		return retrofit.create(serviceClass);
	}
}
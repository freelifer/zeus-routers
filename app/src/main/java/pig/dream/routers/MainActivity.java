package pig.dream.routers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import routers.Routers;

public class MainActivity extends AppCompatActivity {
    ActivityApi api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Retrofit
        Routers routers = new Routers.Builder().setPackageName("pig.dream.routers").build();

        api = routers.create(ActivityApi.class);
    }

    public void toGo(View view) {
//        Intent intent = api.toMainActivity(1, "a");
//        startActivity(intent);

        api.toMainActivity2(1, "a").navigation(this);

//        startActivityForResult();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

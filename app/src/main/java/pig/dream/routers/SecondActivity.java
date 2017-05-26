package pig.dream.routers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.tvHelloWolrd);
        tv.setText("Second Hello World");

        int id = getIntent().getIntExtra("idKey", -1);
        String name = getIntent().getStringExtra("nameKey");


        return;
    }


    public void toGo(View view) {
        setResult(RESULT_OK);
        finish();
    }

}

package com.sifanrpf.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.sifanrpf.database.BonusDataSource;
import com.sifanrpf.database.TesteTeoricoDataSource;
import com.sifanrpf.gradeseeker.Bonus;
import com.sifanrpf.gradeseeker.InputFilterMaxMin;
import com.sifanrpf.gradeseeker.InputFilterMaxMinFloat;
import com.sifanrpf.controlanota.R;
import com.sifanrpf.gradeseeker.TesteTeorico;

public class BonusActivity extends ActionBarActivity{

	private BonusDataSource bonusDataSource;
	private String nomeDisciplina;
	
	private float inputValorBonus = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bonus_activity);
		bonusDataSource = new BonusDataSource(this);
		bonusDataSource.open();
		retiraExtrasIntent();
		
		final TextView nomeDisciplinaText = (TextView) findViewById(R.id.nomeDisciplina);
		nomeDisciplinaText.setText(nomeDisciplina);
		
		final EditText valorBonus = (EditText) findViewById(R.id.valorBonus);
		valorBonus.setFilters(new InputFilter[]{ new InputFilterMaxMinFloat("0", "20")});
		
		final Button button = (Button) findViewById(R.id.gravarBonus);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String bonusStr = valorBonus.getText().toString();
				if(!bonusStr.equals("")){
					inputValorBonus = Float.parseFloat(bonusStr);
					criaBonus();
					Intent intent = new Intent(getBaseContext(), DisciplinaActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("NomeDisciplina", nomeDisciplina);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});		
	}
	
	public void retiraExtrasIntent(){
		Bundle bundle = getIntent().getExtras();
		nomeDisciplina = bundle.getString("NomeDisciplina");
	}
	
	public void criaBonus(){
		Bonus bonus = bonusDataSource.createBonus(nomeDisciplina, "Valor", 0, inputValorBonus);
	}

}

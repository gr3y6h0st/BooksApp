package com.nanodegree.android.booksapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //tag for log messages
    public static final String LOG_TAG = MainActivity.class.getName();

    // Context is referenced by other classes
    public static Context mContext;

    Button searchButton;
    Button clearTitleButton;
    Button clearAuthorButton;

    EditText editTextTitleSearch; //Edit text input obj
    EditText editTextAuthorSearch; //Edit text input obj

    Spinner spinnerOption; //spinner obj

    public static String editTextAuthorInput; //input
    public static String editTextTitleInput; //input
    public static String spinnerSelection; //results filter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // Initialize Views
        editTextTitleSearch = (EditText) findViewById(R.id.search_title_EditText);
        editTextAuthorSearch = (EditText) findViewById(R.id.search_author_EditText);
        searchButton = (Button) findViewById(R.id.search_Button);
        clearTitleButton = (Button) findViewById(R.id.clear_title_button);
        clearAuthorButton = (Button) findViewById(R.id.clear_author_button);

        // Set OnClickListeners
        searchButton.setOnClickListener(this);
        clearTitleButton.setOnClickListener(this);
        clearAuthorButton.setOnClickListener(this);

        // Disable and hide clear buttons if the EditTexts are empty
        if (editTextTitleSearch.getText().length() == 0) {
            clearTitleButton.setEnabled(false);
            clearTitleButton.setVisibility(View.INVISIBLE);
        } else {
            clearTitleButton.setEnabled(true);
            clearTitleButton.setVisibility(View.VISIBLE);
        }

        if (editTextAuthorSearch.getText().length() == 0) {
            clearAuthorButton.setEnabled(false);
            clearAuthorButton.setVisibility(View.INVISIBLE);
        } else {
            clearAuthorButton.setEnabled(true);
            clearAuthorButton.setVisibility(View.VISIBLE);
        }


        // Set TextChanged Listeners
        editTextTitleSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // Whenever EditText has text = reveal "clear" button
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    clearTitleButton.setEnabled(false);
                    clearTitleButton.setVisibility(View.INVISIBLE);
                } else {
                    clearTitleButton.setEnabled(true);
                    clearTitleButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextAuthorSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If there is text in the EditText, show the clear button, if not, hide it
                if (s.toString().trim().length() == 0) {
                    clearAuthorButton.setEnabled(false);
                    clearAuthorButton.setVisibility(View.INVISIBLE);
                } else {
                    clearAuthorButton.setEnabled(true);
                    clearAuthorButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinnerOption = (Spinner) findViewById(R.id.spinner);
        populateSpinner(spinnerOption);

        // OnClickItemListener for spinner option will read user input for results per page
        spinnerOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelection = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinnerSelection = getString(R.string.spinner_default);
            }
        });

    }

    public void populateSpinner(Spinner spinner) {
        // Create ArrayAdapter using the string array (see strings.xml) and run it via default spinner layout
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array
                .spinner_filter_results, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_title_button:
                editTextTitleSearch.setText("");
                break;
            case R.id.clear_author_button:
                editTextAuthorSearch.setText("");
                break;
            case R.id.search_Button:
                // Save input
                editTextTitleInput = editTextTitleSearch.getText().toString();
                editTextAuthorInput = editTextAuthorSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
                break;
        }
    }
}


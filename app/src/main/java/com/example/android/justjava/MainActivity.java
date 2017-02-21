package com.example.android.justjava;

import java.text.NumberFormat;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;



public class MainActivity extends AppCompatActivity {

    static final int MaxNumCoffees = 10;
    int mNumberOfCoffees = 0;
    boolean mbWhippedCream = false;
    boolean mbChocolate = false;
    int mPriceOfWhippedCream = 0;
    int mPriceOfChocolate = 0;
    int mPriceOfCoffey = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        whippedCreamCheckBox.setChecked(false);

        final CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        chocolateCheckBox.setChecked(false);

        final TextView quanityTextView = (TextView) findViewById(R.id.quantity);

        /*
        Get the prices for the add-ons and coffee from the resource files.
         */
        mPriceOfWhippedCream = getResources().getInteger(R.integer.whipped_cream_addon);
        mPriceOfChocolate = getResources().getInteger(R.integer.chocolate_addon);
        mPriceOfCoffey = getResources().getInteger(R.integer.price_of_coffee);

        /*
         Put the prices next to the views
         */
        addPriceToView((TextView) whippedCreamCheckBox, mPriceOfWhippedCream);
        addPriceToView((TextView) chocolateCheckBox, mPriceOfChocolate);
        addPriceToView(quanityTextView, mPriceOfCoffey);


    }

    private void addPriceToView(TextView textView, int price) {
        String text = textView.getText().toString();
        text += " " + NumberFormat.getCurrencyInstance().format(price);
        textView.setText(text);
    }


    public void increment(View view) {
        // Do something in response to the buttom
        if (mNumberOfCoffees != MaxNumCoffees) mNumberOfCoffees++;
        display(mNumberOfCoffees);

    }

    public void decrement(View view) {
        // Do something in response to the buttom
        if (mNumberOfCoffees != 0) mNumberOfCoffees--;
        display(mNumberOfCoffees);
    }

    /**
     * Called when the user clies the Send button
     */
    public void submitOrder(View view) {
        // Do something in response to the buttom
        String message = "";

        // Get the name from the EditText object
        EditText editText = (EditText) findViewById(R.id.name_input);
        String name = editText.getText().toString();
        boolean mOrderValid = false;

        if (mNumberOfCoffees == 0) {
            message = getString(R.string.thank_you_empty);

        } else if (name.isEmpty()) {
            message = getString(R.string.no_name_provided);
        } else {
            mOrderValid = true;
            if (name.isEmpty()) {
                name = getString(R.string.no_name_provided);
            }
            message = getString(R.string.name_hint) + ": " + name + "\n";
            if (mbChocolate || mbWhippedCream) {
                message += getString(R.string.toppings_message);
                if (mbWhippedCream) {
                    message += "\n" + getString(R.string.topping_whipped_cream);
                }
                if (mbChocolate) {
                    message += "\n" + getString(R.string.topping_chocolate);
                }
                message += "\n";
            }
            message += getString(R.string.thank_you_message,
                    NumberFormat.getCurrencyInstance().format(calculatePrice()));

        }
        if (!mOrderValid) {
            message += "\n" + getString(R.string.order_not_processed);
        } else {
            //displayMessage(message);
            String[] addressess = {"bruce.sherry88@gmail.com"};
            composeEmail(addressess, "Coffee Order", message);

        }


    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(message);
    }

    private int calculatePrice() {


        int totalPriceOfCoffey =
                mNumberOfCoffees * (mPriceOfCoffey + (mbWhippedCream ? mPriceOfWhippedCream : 0) + (mbChocolate ? mPriceOfChocolate : 0));
        Log.d("MainActivity", "Total Price Of Coffee: " + totalPriceOfCoffey);
        return totalPriceOfCoffey;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.whipped_cream_checkbox:
                mbWhippedCream = checked;
                break;
            case R.id.chocolate_checkbox:
                mbChocolate = checked;
                break;
        }
    }

    private void composeEmail(String[] addresses, String subject, String textBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, textBody);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}

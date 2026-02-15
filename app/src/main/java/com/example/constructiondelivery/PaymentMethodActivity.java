package com.example.constructiondelivery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;

public class PaymentMethodActivity extends AppCompatActivity {

    private RadioGroup paymentMethodGroup;
    private TextInputLayout transactionIdLayout;
    private EditText transactionIdEditText;
    private ImageView qrCodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        paymentMethodGroup = findViewById(R.id.payment_method_group);
        transactionIdLayout = findViewById(R.id.transaction_id_layout);
        transactionIdEditText = findViewById(R.id.transaction_id_edit_text);
        qrCodeImage = findViewById(R.id.qr_code_image);
        Button placeOrderButton = findViewById(R.id.place_order_button);

        Intent intent = getIntent();
        Material material = (Material) intent.getSerializableExtra("material");

        if (material != null && material.image != 0) {
            qrCodeImage.setImageResource(material.image);
        }

        // Default state
        qrCodeImage.setVisibility(View.GONE);
        transactionIdLayout.setVisibility(View.GONE);

        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.pay_online_radio_button) {
                qrCodeImage.setVisibility(View.VISIBLE);
                transactionIdLayout.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.cod_radio_button) {
                qrCodeImage.setVisibility(View.GONE);
                transactionIdLayout.setVisibility(View.GONE);
            }
        });

        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();

        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        String paymentMethod;
        String transactionId = "";

        if (selectedPaymentMethodId == R.id.pay_online_radio_button) {
            paymentMethod = "Online";
            transactionId = transactionIdEditText.getText().toString().trim();
            if (transactionId.isEmpty()) {
                transactionIdEditText.setError("Transaction ID is required");
                transactionIdEditText.requestFocus();
                return;
            }
        } else {
            paymentMethod = "COD";
        }

        // TODO: Save order details to the database with the payment method and transaction ID

        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(PaymentMethodActivity.this, OrdersActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 3000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

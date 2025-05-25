package com.example.calculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private double factorial = 0;
    private double power = 0;
    private double squareRoot = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText = findViewById(R.id.editText1);
    }


    public int calculateFactorial(int num)
    {
        int fact = 1;

        for (int i = 1; i <= num; i++)
        {
            fact *= i;
        }
        return  fact;
    }

    public double calculate(String text)
    {
        String num1S = "";
        String num2S = "";
        double num1 = 0;
        double num2 = 0;
        char operation = 0;

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '!' || c == '√') {
                operation = c;
            }
            else if ((Character.isDigit(c) || c == '.') && operation == 0)
            {
                num1S += c;
            }
            else if ((Character.isDigit(c) || c == '.') && operation != 0)
            {
                num2S += c;
            }
        }

        if (!num1S.isEmpty()) {
            num1 = Double.parseDouble(num1S);
        }

        // Handle the case where num2 is empty (no second number)
        if (!num2S.isEmpty()) {
            num2 = Double.parseDouble(num2S);
        }

        System.out.println("Num1 : " + num1 + "\nNum2 : " + num2);

        if (operation == '+')
            return (num1 + num2);
        else if (operation == '-')
            return (num1 - num2);
        else if (operation == '*')
            return (num1 * num2);
        else if (operation == '/')
            return (double)(num1 / num2);
        else if (operation == '^')
            return Math.pow(num1, num2);
        else if (operation == '!')
            return (calculateFactorial((int) num1));
        else if (operation == '√')
            return Math.sqrt(num2);

        return 0;
    }

    public void handleButtonClick(View view)
    {
        int id = view.getId();

        if (id == R.id.button0)
            editText.append("0");
        else if (id == R.id.button1)
            editText.append("1");
        else if (id == R.id.button2)
            editText.append("2");
        else if (id == R.id.button3)
            editText.append("3");
        else if (id == R.id.button4)
            editText.append("4");
        else if (id == R.id.button5)
            editText.append("5");
        else if (id == R.id.button6)
            editText.append("6");
        else if (id == R.id.button7)
            editText.append("7");
        else if (id == R.id.button8)
            editText.append("8");
        else if (id == R.id.button9)
            editText.append("9");
        else if (id == R.id.buttonPlus)
            editText.append("+");
        else if (id == R.id.buttonMinus)
            editText.append("-");
        else if (id == R.id.buttonMultiply)
            editText.append("*");
        else if (id == R.id.buttonDivide)
            editText.append("/");
        else if (id == R.id.buttonComma)
            editText.append(".");
        else if (id == R.id.buttonEqual)
        {
            double result = calculate(editText.getText().toString().trim());
            editText.setText(String.valueOf(result));
        }
        else if (id == R.id.buttonClear)
            editText.setText("");
        else if (id == R.id.buttonFactorial)
            editText.append("!");
        else if (id == R.id.buttonPower)
            editText.append("^");
        else if (id == R.id.buttonSRoot)
            editText.append("√");

    }
}
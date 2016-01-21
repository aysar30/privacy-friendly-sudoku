package org.secuso.privacyfriendlysudoku.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.secuso.privacyfriendlysudoku.controller.GameController;
import org.secuso.privacyfriendlysudoku.controller.Symbol;
import org.secuso.privacyfriendlysudoku.game.listener.IHighlightChangedListener;

/**
 * Created by TMZ_LToP on 12.11.2015.
 */


public class SudokuKeyboardLayout extends LinearLayout implements IHighlightChangedListener {

    AttributeSet attrs;
    SudokuButton [] buttons;
    GameController gameController;
    Symbol symbolsToUse = Symbol.Default;
    float normalTextSize = 0;
    LinearLayout [] layouts = new LinearLayout[2];

    OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v instanceof SudokuButton) {
                SudokuButton btn = (SudokuButton)v;

                gameController.selectValue(btn.getValue());

                gameController.saveGame(getContext());
            }
        }
    };


    public SudokuKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
    }

    public void setSymbols(Symbol s) {
        symbolsToUse = s;
        for(SudokuButton b : buttons) {
            b.setText(Symbol.getSymbol(symbolsToUse, b.getValue()-1));
        }
    }

    public void setKeyBoard(int size,int width, int height) {
        LayoutParams p;
        buttons = new SudokuButton[size];
        int number = 0;
        int torun = (size % 2 == 0) ? size/2 :(size+1)/2 ;
        int realSize = torun;


        //set layout parameters and init Layouts
        for (int i = 0; i < 2; i++) {
            p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            //if (i == 0) p.bottomMargin=10; else p.topMargin=10;
            p.setMargins(0,5,0,5);
            layouts[i] = new LinearLayout(getContext(),null);
            layouts[i].setLayoutParams(p);
            layouts[i].setWeightSum(torun);
            layouts[i].setOrientation(LinearLayout.HORIZONTAL);
            addView(layouts[i]);
        }

        //int width2 =(width-(realSize*30))/(realSize);



        for (int k = 0; k<2;k++){
            for (int i = 0; i< torun; i++){
                if (number == size) {
                    break;
                }
                buttons[number] = new SudokuButton(getContext(),null);
                p = new LayoutParams(0, LayoutParams.MATCH_PARENT,1);
                p.setMargins(5,5,5,5);
                buttons[number].setLayoutParams(p);
                /* removed GridLayout because of bad scaling will use now a Linearlayout
                Spec rowSpec = spec(k,1);
                Spec colSpec = spec(i,1);

                p = (new LayoutParams(rowSpec,colSpec));

                //p = new LayoutParams(rowSpec,colSpec);
                p.setMargins((i == 0) ? 0 : 5,5,5,5);
                p.width = (width - (int)((getResources().getDimension(R.dimen.activity_horizontal_margin))*2)) / realSize;
                p.width = p.width - 10;
                //p.setGravity(Gravity.FILL_VERTICAL);
                //p.setGravity(Gravity.FILL);
               // p.setGravity(LayoutParams.WRAP_CONTENT);
                */

          //      buttons[number].setLayoutParams(p);
                //buttons[number].setGravity(Gravity.CENTER);
                buttons[number].setType(SudokuButtonType.Value);
                buttons[number].setTextColor(getResources().getColor(R.color.white));
                buttons[number].setBackgroundResource(R.drawable.mnenomic_numpad_button);
                buttons[number].setText(Symbol.getSymbol(symbolsToUse, number));
                buttons[number].setValue(number + 1);
                buttons[number].setOnClickListener(listener);
                layouts[k].addView(buttons[number]);
                number++;
            }
        }
    }

    public void setButtonsEnabled(boolean enabled) {
        for(SudokuButton b : buttons) {
            b.setEnabled(enabled);
        }
    }

    public void setGameController(GameController gc){
        if(gc == null) {
            throw new IllegalArgumentException("GameController may not be null.");
        }

        gameController = gc;
        gameController.registerHighlightChangedListener(this);
    }

    public void updateNotesEnabled() {
        if (normalTextSize == 0) {
            normalTextSize = buttons[0].getTextSize();
        }

        if(gameController.getNoteStatus()) {
            setTextSize(normalTextSize*0.6f);
        } else {
            setTextSize(normalTextSize);
        }
    }

    private void setTextSize(float size){
        for (SudokuButton b : buttons){
            //b.setTextSize(size);
            b.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        }
    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onHighlightChanged() {
        for(SudokuButton i_btn : buttons) {
            i_btn.setBackgroundResource(R.drawable.mnenomic_numpad_button);

            // Highlight Yellow if we are done with that number
            if(gameController.getValueCount(i_btn.getValue()) == gameController.getSize()) {
                i_btn.setBackgroundResource(R.drawable.numpad_highlighted_three);
            }

            if(gameController.getSelectedValue() == i_btn.getValue()) {
                // highlight button to indicate that the value is selected
                i_btn.setBackgroundResource(R.drawable.numpad_highlighted);
            }
        }
    }
    public void fixHeight (){
        int i = getHeight();
        i = buttons[0].getHeight();
        i = buttons[5].getHeight();
    }
}
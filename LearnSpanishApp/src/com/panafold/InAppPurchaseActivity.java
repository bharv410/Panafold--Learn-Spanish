package com.panafold;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.panafold.main.MainActivity;

public class InAppPurchaseActivity extends Activity implements BillingProcessor.IBillingHandler{
	BillingProcessor bp;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_in_app_purchase);
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqiLU0GwvBQu7VQTN821qMfmjaec2DKksSfXU8klufTp8H0nPoVnufdb87W5PVIttNWfOQK+3SO+ZTfPNCPYZWf5RBDR9U6Km/jMPxhQ526NdYf9Q4PyBBJDlo96ycDxBdjgi7yoCSfdVsCKgBuThAjsdcUmHrdRMAQIBN9b8IGFH2lhtgQHHbvHXz9k4Vyx/xjMw3YJHaOmh9RtZTKB944u9i1AFVa+YCisvVabeIafV+vcG2D2LdyucWcuG+3LROn8EZhyC3ByJNuexebTKg/7KqWD826bh6o5Wg0AnOa2AdnsyXl18S19oZ44QkKfM7IOpSlB+W4JqXbc7gDaxkwIDAQAB";
		bp = new BillingProcessor(this, base64EncodedPublicKey, this);
		showDialog();
	}
	
	public void buyClick(View v){
		bp.purchase("com.panafold.allwords");
		System.out.println("clicked");
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
	
	@Override
    public void onBillingInitialized() {
        System.out.println("onBillingInitialized");
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails td) {
        /*
         * Called then requested PRODUCT ID was successfully purchased
         */
    	System.out.println("onProductPurchased");
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called then some error occured. See Constants class for more details
         */
    	System.out.println("onBillingError");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called then purchase history was restored and the list of all owned PRODUCT ID's 
         * was loaded from Google Play
         */
    	System.out.println("onPurchaseHistoryRestored");
    }
    @Override
    public void onDestroy() {
        if (bp != null) 
            bp.release();

        super.onDestroy();
    }
    public void goBack(View v){
    	startActivity(new Intent(InAppPurchaseActivity.this,MainActivity.class));
	}
    
    private void showDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				InAppPurchaseActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Purchase More Words?");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Do you want to purchase all words and phrases rather than recieving 1 per day?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						
						bp.purchase("com.panafold.allwords");
						System.out.println("clicked");
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						startActivity(new Intent(InAppPurchaseActivity.this,MainActivity.class));
						
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    }
}

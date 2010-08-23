/**
 * @author Yixin Zhu
 */
package com.comic.viewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comic.globals.Globals;

public class MainPage extends ListActivity {
	//list of volume names
	private String[] volumeNames = {Globals.VolFiveName, 
			Globals.VolFourName, Globals.VolThreeName, 
			Globals.VolTwoName, Globals.VolOneName, Globals.VolZeroName};
	//list of volume info
	private String[] volumeInfo = {Globals.VolFiveInfo, 
			Globals.VolFourInfo, Globals.VolThreeInfo, 
			Globals.VolTwoInfo, Globals.VolOneInfo, Globals.VolZeroInfo};
	private final String copyrightBundleKey = "copyrightDialogBundle";
	private final String helpBundleKey = "helpDialogBundle";
	private AlertDialog copyrightDialog, helpDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		//set the main menu list
		setListAdapter(new ListVolumesAdapter(this));
		//handle for screen orientation change
		if (savedInstanceState != null){
			if (savedInstanceState.getBundle(copyrightBundleKey) != null) {
				buildCopyrightDialog(Globals.CopyrightTitle, Globals.CopyrightMessage);
			} else if (savedInstanceState.getBundle(helpBundleKey) != null){
				buildHelpDialog(Globals.HelpTitle);
			}
		}
	}
	
	/**
	 * Launches the corresponding volume based on user selection
	 * 
	 * @param volumeNameIndex The index of the user selection
	 */
	public void launchVolume(int volumeNameIndex){
		Toast.makeText(this, "Beginning Volume- "+ (5 - volumeNameIndex), 1000).show();
		Intent i = new Intent(this, ComicViewer.class);
		switch (volumeNameIndex){
			case 5:
				i.putExtra("volumeRange", Globals.ZeroRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
			case 4:
				i.putExtra("volumeRange", Globals.OneRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
			case 3:
				i.putExtra("volumeRange", Globals.TwoRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
			case 2:
				i.putExtra("volumeRange", Globals.ThreeRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
			case 1:
				i.putExtra("volumeRange", Globals.FourRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
			case 0:
				i.putExtra("volumeRange", Globals.FiveRange);
				i.putExtra("volumeNumber", (5 - volumeNameIndex));
				break;
		}
		startActivity(i);
	}
	
	/**
	 * used to create an options menu
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(Menu.NONE, Globals.HELP_ID, Menu.NONE, "Help");
    	menu.add(Menu.NONE, Globals.COPYRIGHT_ID, Menu.NONE, "Copyright");
    	return super.onCreateOptionsMenu(menu);
    }
	/**
	 * called when an option is selected
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	return(applyMenuChoice(item) || super.onOptionsItemSelected(item));
    }
    /**
     * performs the action on a selected item choice
     * @param item the id of the item selected
     * @return true if the item selected was performed
     */
	private boolean applyMenuChoice(MenuItem item) {
		switch(item.getItemId())
		{
		case Globals.HELP_ID: //display help menu
			buildHelpDialog(Globals.HelpTitle);
			return true;
		case Globals.COPYRIGHT_ID: //display copyright information
			buildCopyrightDialog(Globals.CopyrightTitle, Globals.CopyrightMessage);
			return true;
		}
		return false;
	}
	
	/**
	 * builds an help alert dialog displaying a title and message
	 * @param title: the title of the alert dialog
	 */
	private void buildHelpDialog(String title){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.help_dialog_context,
                (ViewGroup) findViewById(R.id.layout_root));
		builder.setTitle(title);
		builder.setView(layout);
		builder.setNegativeButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		helpDialog = builder.create();
		helpDialog.show();
	}
	
	/**
	 * builds an copyright alert dialog displaying a title and message
	 * @param title: the title of the alert dialog
	 * @param message: the content of the alert dialog
	 */
	private void buildCopyrightDialog(String title, String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.copyright_dialog_context,
                (ViewGroup) findViewById(R.id.layout_root));
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);
		builder.setTitle(title);
		builder.setView(layout);
		builder.setNegativeButton("OK", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		copyrightDialog = builder.create();
		copyrightDialog.show();
	}
	
	/**
	 * called when screen orientation is changed
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (copyrightDialog != null && copyrightDialog.isShowing()){
			outState.putBundle(copyrightBundleKey, copyrightDialog.onSaveInstanceState());
		} else if (helpDialog != null && helpDialog.isShowing()){
			outState.putBundle(helpBundleKey, helpDialog.onSaveInstanceState());
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position,long id){
		launchVolume(position); //perform action based on user click
	}
	
	/**
	 * Custom adapter for custom list view
	 * 
	 * @author Yixin Zhu
	 */
	class ListVolumesAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater mInflater;

		public ListVolumesAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
		}
		
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.main_context,
						null);
				// Creates a ViewHolder and store references to the two children
				// views we want to bind data to.
				holder = new ViewHolder();
				holder.volumeName = (TextView) convertView
						.findViewById(R.id.volumeName);
				holder.volumeDescription = (TextView) convertView
						.findViewById(R.id.volumeDiscription);
				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}
			holder.volumeName.setText(volumeNames[position]);
			holder.volumeDescription.setText(volumeInfo[position]);
			return convertView;
		}

		class ViewHolder {
			TextView volumeName;
			TextView volumeDescription;
		}

		public Filter getFilter() {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return volumeNames.length;
		}

		public Object getItem(int position) {
			return position;
		}

	}
}

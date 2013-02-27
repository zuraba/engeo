package ge.android.engeo;

import ge.android.engeo.R;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.sqlite.SQLiteCursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class Wordlist extends ListActivity implements OnItemClickListener,
		OnItemLongClickListener, OnSharedPreferenceChangeListener,
		OnTouchListener {

	private EditText editText;
	private Drawable x, s;

	/** Conversations. */
	private WordlistAdapter adapter = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordlist);

		final ListView list = this.getListView();
		this.adapter = new WordlistAdapter(this);
		this.setListAdapter(this.adapter);

		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);

		x = getResources().getDrawable(R.drawable.close);
		x.setBounds(0, 0, x.getIntrinsicWidth(), x.getIntrinsicHeight());
		s = getResources().getDrawable(R.drawable.search);
		s.setBounds(0, 0, s.getIntrinsicWidth(), s.getIntrinsicHeight());

		editText = (EditText) this.findViewById(R.id.textEdit);
		editText.addTextChangedListener(adapter);
		editText.setCompoundDrawables(s, null, editText.getText().toString()
				.equals("") ? null : x, null);
		editText.setOnTouchListener(this);
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence st, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				editText.setCompoundDrawables(s, null, editText.getText()
						.toString().equals("") ? null : x, null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		registerForContextMenu(list);
	}

	@Override
	protected void onDestroy() {
		adapter.db.close();
		super.onDestroy();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		return parent.showContextMenu();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent detail = new Intent(view.getContext(), DetailView.class);
		SQLiteCursor c = (SQLiteCursor) adapter.getItem(position);
		final String[] extras = new String[] {
				c.getString(c.getColumnIndex("original")),
				c.getString(c.getColumnIndex("transcription")),
				c.getString(c.getColumnIndex("name")),
				c.getString(c.getColumnIndex("translate")) };
		detail.putExtra("extras", extras);
		startActivityForResult(detail, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean onCreateOptionsMenu(final Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		prefs();
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.share:
			share(info);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void share(AdapterContextMenuInfo info) {
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Some text");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"Some other text");
		startActivity(Intent.createChooser(shareIntent, "Title for chooser"));
	}

	public SharedPreferences prefs() {
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
		return PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	public final boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_settings: // start settings activity
			this.startActivity(new Intent(this, Preferences.class));
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (editText.getCompoundDrawables()[2] == null) {
			return false;
		}
		if (event.getAction() != MotionEvent.ACTION_UP) {
			return false;
		}
		if (event.getX() > editText.getWidth() - editText.getPaddingRight()
				- x.getIntrinsicWidth()) {
			editText.setText("");
			editText.setCompoundDrawables(s, null, null, null);
		}
		return false;
	}
}
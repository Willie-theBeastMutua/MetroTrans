package com.example.metrotrans;

import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
	@SuppressWarnings("unused")
	private static final String TAG = SelectableAdapter.class.getSimpleName();

	private SparseBooleanArray selectedItems;
	DatabaseReference reference;
	private FirebaseAuth mAuth;
	private String selseats;
	private JSONArray jsonArray;
	private SparseBooleanArray setsele;
	private int seatssel;
	ArrayList<seatClass> sched;
	public SelectableAdapter() {
		selectedItems = new SparseBooleanArray ();

					selectedItems.append(1 , true);




	}

	/**
	 * Indicates if the item at position position is selected
	 * @param position Position of the item to check
	 * @return true if the item is selected, false otherwise
	 */
	public boolean isSelected(int position) {
		return getSelectedItems().contains(position);
	}

	/**
	 * Toggle the selection status of the item at a given position
	 * @param position Position of the item to toggle the selection status for
	 */
	public void toggleSelection(int position) {
		if (selectedItems.get(position, false)) {
			selectedItems.delete(position);
		} else {
			selectedItems.put(position, true);
			String positions = Integer.toString(position);
			Log.w("position", positions);



		}
        String areay = Integer.toString(position);
        Log.w("Array", areay);
		notifyItemChanged(position);
		seatClass seat = new seatClass(
			areay
		);
		FirebaseDatabase.getInstance().getReference().child("Seat").push().setValue(seat);

	}

	/**
	 * Clear the selection status for all items
	 */
	public void clearSelection() {
		List<Integer> selection = getSelectedItems();
		selectedItems.clear();
		for (Integer i : selection) {
			notifyItemChanged(i);
		}
	}

	/**
	 * Count the selected items
	 * @return Selected items count
	 */
	public int getSelectedItemCount() {
		return selectedItems.size();
	}

	/**
	 * Indicates the list of selected items
	 * @return List of selected items ids
	 */
	public List<Integer> getSelectedItems() {
		List<Integer> items = new ArrayList<> (selectedItems.size());
		for (int i = 0; i < selectedItems.size(); ++i) {
			items.add(selectedItems.keyAt(i));
		}
		return items;
	}
}
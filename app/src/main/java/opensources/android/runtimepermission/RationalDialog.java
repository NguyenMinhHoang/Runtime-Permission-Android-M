package opensources.android.runtimepermission;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RationalDialog extends DialogFragment implements View.OnClickListener{
    private static final String CONST_DATA = "data";
    private static final String CONST_PERMISSION_NAME = "permission_name";


    private ArrayList<RationalItemModel> mData;
    private String[] mPermissionName;

    private OnDialogInteractionListener mListener;

    public RationalDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data
     * @param permissionName
     * @return A new instance of fragment RationalDialog.
     */
    public static RationalDialog newInstance(String[] permissionName, ArrayList<RationalItemModel> data) {
        RationalDialog fragment = new RationalDialog();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CONST_DATA, data);
        args.putStringArray(CONST_PERMISSION_NAME, permissionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mData = getArguments().getParcelableArrayList(CONST_DATA);
            mPermissionName = getArguments().getStringArray(CONST_PERMISSION_NAME);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_rational_dialog, container, false);
        RecyclerView rv = (RecyclerView) root.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new DetailAdapter(mData));
        root.findViewById(R.id.btn_ok).setOnClickListener(this);
        root.findViewById(R.id.btn_cancel).setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View view) {
        mListener.onDialogInteraction(this, view, mPermissionName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogInteractionListener) {
            mListener = (OnDialogInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDialogInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setListener(OnDialogInteractionListener listener) {
        this.mListener = listener;
    }

    public class DetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private ArrayList<RationalItemModel> mData;

        public DetailAdapter(ArrayList<RationalItemModel> data) {
            mData = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolderItem holder = new ViewHolderItem(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_rational_item, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ViewHolderItem viewHolder = (ViewHolderItem) holder;
            viewHolder.getTvTitle().setText(mData.get(position).getTitle());
            viewHolder.getTvMessage().setText(mData.get(position).getMessage());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public class ViewHolderItem extends RecyclerView.ViewHolder {

            private TextView mTvTitle;
            private TextView mTvMessage;

            public ViewHolderItem(View root) {
                super(root);
                mTvTitle = (TextView)root.findViewById(R.id.tv_title);
                mTvMessage = (TextView)root.findViewById(R.id.tv_message);
            }

            public TextView getTvTitle() {
                return mTvTitle;
            }

            public TextView getTvMessage() {
                return mTvMessage;
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDialogInteractionListener {
        void onDialogInteraction(RationalDialog dialog, View v, String[] permissionName);
    }

}



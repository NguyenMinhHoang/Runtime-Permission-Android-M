package opensources.android.runtimepermission;


import android.os.Parcel;
import android.os.Parcelable;

public class RationalItemModel implements Parcelable {
    private String title;
    private String message;

    public RationalItemModel(){}

    public RationalItemModel(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
    }

    protected RationalItemModel(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
    }

    public static final Parcelable.Creator<RationalItemModel> CREATOR = new Parcelable.Creator<RationalItemModel>() {
        @Override
        public RationalItemModel createFromParcel(Parcel source) {
            return new RationalItemModel(source);
        }

        @Override
        public RationalItemModel[] newArray(int size) {
            return new RationalItemModel[size];
        }
    };
}

package com.example.sarthak.hms.services;

import android.os.Handler;
import android.util.Log;

import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.callbacks.IComplaintCategoriesListCallback;
import com.example.sarthak.hms.callbacks.IComplaintListCallback;
import com.example.sarthak.hms.callbacks.IComplaintPicturesListCallback;
import com.example.sarthak.hms.callbacks.IComplaintTitleListCallback;
import com.example.sarthak.hms.models.*;
import com.example.sarthak.hms.retrofit.IComplaintsService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComplaintsService {

    private List<Complaint> getDummyComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        try {
            Student s = new Student();
            s.setId(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");

            Complaint c = new Complaint();
            c.setId(5);
            c.setTitle("Water tap leaking");
            c.setStudent(s);
            ComplaintCategory cc = new ComplaintCategory();
            cc.setId(2);
            cc.setName("Plumber");
            cc.setCode("plumb");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-24T08:45:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-10-24T00:00:00"));
            c.setAppointmentFromTimePreference(stf.parse("12:15:00"));
            c.setAppointmentToTimePreference(stf.parse("13:00:00"));
            c.setDescription("The tap has been leaking since 6 in the morning.");
            c.setFeedback("");
            c.setComplaintStatus(0);
            c.setStarred(true);
            complaints.add(c);

            c = new Complaint();
            c.setId(4);
            c.setTitle("Washing machine not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(3);
            cc.setName("Housekeeping");
            cc.setCode("house");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-20T19:15:00"));
            c.setAppointmentDatePreference(null);
            c.setAppointmentFromTimePreference(null);
            c.setAppointmentToTimePreference(null);
            c.setDescription("When I put my clothes in, the machine was working fine. But when I went to take them out after about an hour, the clothes were completely dry and it looked like the machine didn't run at all.");
            c.setFeedback("");
            c.setComplaintStatus(1);
            c.setStarred(false);
            complaints.add(c);

            c = new Complaint();
            c.setId(3);
            c.setTitle("Door handle broken");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(1);
            cc.setName("Carpenter");
            cc.setCode("carp");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-04T15:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-01-06T00:10:00"));
            c.setAppointmentFromTimePreference(stf.parse("10:00:00"));
            c.setAppointmentToTimePreference(stf.parse("11:00:00"));
            c.setDescription("The door handle is jammed. Tried to oil it but didn't work out.");
            c.setFeedback("The handle has been fixed now");
            c.setComplaintStatus(2);
            c.setStarred(false);
            complaints.add(c);

            c = new Complaint();
            c.setId(2);
            c.setTitle("Router not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(4);
            cc.setName("Technical Support");
            cc.setCode("tech");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-09-15T12:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-09-16T00:09:00"));
            c.setAppointmentFromTimePreference(stf.parse("12:00:00"));
            c.setAppointmentToTimePreference(stf.parse("13:00:00"));
            c.setDescription("The Wifi disconnects within 5 minutes when I try to connect. This happens every single time. Mostly it shows \"disconnected by admin\", but it even disconnects without any message sometimes, even when using the Network Client on Windows. Updated the network drivers as suggest, but the problem still persists.");
            c.setFeedback("");
            c.setComplaintStatus(1);
            c.setStarred(true);
            complaints.add(c);

            c = new Complaint();
            c.setId(1);
            c.setTitle("AC not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(5);
            cc.setName("Electrician");
            cc.setCode("elec");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-08-29T11:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-08-30T00:08:00"));
            c.setAppointmentFromTimePreference(stf.parse("13:00:00"));
            c.setAppointmentToTimePreference(stf.parse("14:00:00"));
            c.setDescription("The indicator lights on the AC start blinking whenever I turn it on, and then they go off in a few minutes.");
            c.setFeedback("Everything works fine now");
            c.setComplaintStatus(2);
            c.setStarred(false);
            complaints.add(c);
        } catch (ParseException e) {
            Log.e("HMS", e.getMessage());
        }

        return complaints;
    }

    public void getComplaintsByStudentAsync(int sid, final IComplaintListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getComplaintsByStudent(sid).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                callback.onComplaintsList(response.body());
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getComplaintsByStudentAsync(int sid, final IComplaintListCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onComplaintsList(getDummyComplaints());
                }
            }, 2000);
        } else {
            getComplaintsByStudentAsync(sid, callback);
        }
    }

    public void getComplaintsByStudentAsync(int sid, int len, final IComplaintListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getComplaintsByStudent(sid, len).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaintsList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getComplaintsByStudentAsync(int sid, final int len, final IComplaintListCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onComplaintsList(getDummyComplaints().subList(0, len));
                }
            }, 2000);
        } else {
            getComplaintsByStudentAsync(sid, len, callback);
        }
    }

    public void getComplaintById(int id, final IComplaintCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getComplaintById(id).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaint(response.body());
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void updateComplaintStarStatus(int cid, boolean star, final IComplaintCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.updateComplaintStarStatus(cid, star ? 1 : 0).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaint(response.body());
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getComplaintById(final int id, final IComplaintCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Complaint> complaints = getDummyComplaints();
                    for (Complaint complaint : complaints) {
                        if (complaint.getId() == id) {
                            callback.onComplaint(complaint);
                            break;
                        }
                    }
                }
            }, 2000);
        } else {
            getComplaintById(id, callback);
        }
    }

    public void getComplaintPictures(int id, final IComplaintPicturesListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getComplaintPictures(id).enqueue(new Callback<List<ComplaintPicture>>() {
            @Override
            public void onResponse(Call<List<ComplaintPicture>> call, Response<List<ComplaintPicture>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaintPicturesList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ComplaintPicture>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getDefaultComplaintTitles(int id, final IComplaintTitleListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getDefaultComplaintTitles(id).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onTitles(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getAllComplaintCategories(final IComplaintCategoriesListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getAllComplaintCategories().enqueue(new Callback<List<ComplaintCategory>>() {
            @Override
            public void onResponse(Call<List<ComplaintCategory>> call, Response<List<ComplaintCategory>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaintCategoriesList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ComplaintCategory>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void createComplaint(Complaint complaint, final IComplaintCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.createComplaint(complaint).enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaint(response.body());
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getComplaintsByCaretaker(int cid, int len, final IComplaintListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintsService service = retrofit.create(IComplaintsService.class);
        service.getComplaintsByCaretaker(cid, len).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onComplaintsList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }
}

package com.abood.blog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AnswerFragment extends DialogFragment {

    public static final String EXTRA_ANSWER =
            "com.Abood.android.criminal.answer";
    private static final String ARG_ANSWER = "answer";

    private RecyclerView answerRecyclerView;
    private EditText answer;
    AnswerAdapter answerAdapter;
    ArrayList<Answers> answers = new ArrayList<>();
    String ans;
    String id;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_answer, null);

        answer = v.findViewById(R.id.add_answer);
        answerRecyclerView = v.findViewById(R.id.answer_recycler_view);
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        id = getArguments().getString(ARG_ANSWER);

        loadMore();

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Answers")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (!answer.getText().toString().equals(" ")){

                                    ans = answer.getText().toString();
                                    addAnswer();

                                } else {
                                    Toast.makeText(getActivity(), "Enter UserName and Password", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                .create();

    }


    public void loadMore(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST,AppServer.IP+"answers.php", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        answers.add(Answers.createTask(jsonObject));

                    }

                    answerAdapter = new AnswerAdapter(answers,getActivity());
                    answerRecyclerView.setAdapter(answerAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("qqq",response);

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Connection Erorr"+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> info = new HashMap<>();
                info.put("pid",id);
                return info;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    private void sendResult(int resultCode, String answer) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANSWER, answer);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }


    public static AnswerFragment newInstance(String p) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_ANSWER, p);
        AnswerFragment fragment = new AnswerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public class AnswerAdapter extends RecyclerView.Adapter<AnswerViewHolder> {

        ArrayList<Answers> answers;
        Context context;

        public AnswerAdapter(ArrayList<Answers> answers, Context context) {
            this.answers = answers;
            this.context = context;
        }

        @NonNull
        @Override
        public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.answer_holder,parent,false);
            return new AnswerViewHolder(v);

        }

        @Override
        public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {

            holder.user.setText(answers.get(position).getaUser());
            String formatDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(answers.get(position).getDate());
            holder.date.setText(formatDate);
            holder.answer.setText(answers.get(position).getaContent());

        }

        @Override
        public int getItemCount() {
            return answers.size();
        }
    }




    public class AnswerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView user,date,answer;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            user = itemView.findViewById(R.id.user);
            date = itemView.findViewById(R.id.date);
            answer = itemView.findViewById(R.id.answer);

        }

        @Override
        public void onClick(View view) {
//            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getcId());
//            startActivity(intent);
        }
    }


    public void addAnswer(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppServer.IP+"addAnswer.php", null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "connection Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> info = new HashMap<>();
                String name = Login.userName;
                Date date = new Date();
                String formatDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);


                info.put("name",name);
                info.put("date",formatDate);
                info.put("content",ans);
                info.put("pid",id);


                return info;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


}

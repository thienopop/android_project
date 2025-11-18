package com.example.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login.R;
import com.example.login.adapter.ChatsAdapter_st;
import com.example.login.api.ApiService;
import com.example.login.api.RetrofitClient;
import com.example.login.model.ChatWithUserDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragment_st extends Fragment implements ChatsAdapter_st.OnItemClickListener {

    private RecyclerView recyclerView;
    private ChatsAdapter_st adapter;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvChats);
        searchView = view.findViewById(R.id.svChats);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatsAdapter_st(new ArrayList<>());
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        loadChats();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
    }

    private void loadChats() {
        ApiService api = RetrofitClient.getClient(getContext()).create(ApiService.class);
        api.getMyChats().enqueue(new Callback<List<ChatWithUserDetail>>() {
            @Override
            public void onResponse(Call<List<ChatWithUserDetail>> call, Response<List<ChatWithUserDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ChatWithUserDetail>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onItemClick(ChatWithUserDetail item) {
        if (getActivity() != null) {
            androidx.fragment.app.Fragment chat = ChatboxFragment_st.newInstance(
                    item.getChatId(), item.getUserId(), item.getUsername(), item.getFullName());
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, chat)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

package com.example.kuit4_android_retrofit

import RVPopularMenuAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.kuit4_android_retrofit.data.CategoryData
import com.example.kuit4_android_retrofit.data.MenuData
import com.example.kuit4_android_retrofit.databinding.FragmentHomeBinding
import com.example.kuit4_android_retrofit.databinding.ItemCategoryBinding
import com.example.kuit4_android_retrofit.retrofit.RetrofitObject
import com.example.kuit4_android_retrofit.retrofit.service.CategoryService
import com.example.kuit4_android_retrofit.retrofit.service.PopularMenuService
import retrofit2.Call
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvAdapterPopular: RVPopularMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        fetchCategoryInfo()
        fetchPopularMenuData()

        return binding.root
    }

    private fun fetchCategoryInfo() {
        val service = RetrofitObject.retrofit.create(CategoryService::class.java)
        val call = service.getCategories()

        //비동기 통신
        call.enqueue(
            object : retrofit2.Callback<List<CategoryData>>{
                override fun onResponse(
                    call: Call<List<CategoryData>>,
                    response: Response<List<CategoryData>>
                ) {
                    if(response.isSuccessful){
                        //여기서 body는 서버에서 보낸 data
                        val categoryResponse = response.body()

                        //data 성공적으로 받아온 경우
                        if(!categoryResponse.isNullOrEmpty()){
                            showCategoryInfo(categoryResponse)
                        }else{
                            Log.d("fail","response is null or empty")
                        }
                    }else{
                        Log.d("fail","response failure") //보통 상태코드 5xx
                    }
                }

                override fun onFailure(call: Call<List<CategoryData>>, t: Throwable) {
                    Log.d("fail", "network failure")
                }
            }
        )
    }

    private fun showCategoryInfo(categoryList: List<CategoryData>) {
        // 레이아웃 인플레이터를 사용해 카테고리 항목을 동적으로 추가
        val inflater = LayoutInflater.from(requireContext())
        binding.llMainMenuCategory.removeAllViews() // 기존 항목 제거

        categoryList.forEach { category ->
            val categoryBinding = ItemCategoryBinding.inflate(inflater, binding.llMainMenuCategory, false)

            // 이미지 로딩: Glide 사용 (이미지 URL을 ImageView에 로드)
            Glide
                .with(this)
                .load(category.categoryImg)
                .into(categoryBinding.sivCategoryImg)

            // 카테고리 이름 설정
            categoryBinding.tvCategoryName.text = category.categoryName

            // 레이아웃에 카테고리 항목 추가
            binding.llMainMenuCategory.addView(categoryBinding.root)
        }
    }

    private fun fetchPopularMenuData(){
        val service = RetrofitObject.retrofit.create(PopularMenuService::class.java)
        val call = service.getCategories()

        //비동기 통신
        call.enqueue(
            object : retrofit2.Callback<List<MenuData>>{
                override fun onResponse(
                    call: Call<List<MenuData>>,
                    response: Response<List<MenuData>>
                ) {
                    if(response.isSuccessful){
                        //여기서 body는 서버에서 보낸 data
                        val menuResponse = response.body()

                        //data 성공적으로 받아온 경우
                        if(!menuResponse.isNullOrEmpty()){
                            initRVPopularMenu(menuResponse)
                        }else{
                            Log.d("fail","response is null or empty")
                        }
                    }else{
                        Log.d("fail","response failure") //보통 상태코드 5xx
                    }
                }

                override fun onFailure(call: Call<List<MenuData>>, t: Throwable) {
                    Log.d("fail", "network failure")
                }

            }
        )
    }

    private fun initRVPopularMenu(popularList: List<MenuData>){
        rvAdapterPopular = RVPopularMenuAdapter(requireContext(),popularList)
        with(binding.rvMainPopularMenus){
            adapter = rvAdapterPopular
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

}

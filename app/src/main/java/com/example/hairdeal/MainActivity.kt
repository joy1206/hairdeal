package com.example.hairdeal

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hairdeal.R
import com.example.hairdeal.ui.theme.HairDealTheme
import com.example.hairdeal.ui.theme.NotoSansKr

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HairDealTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HairDealScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun HairDealScreen(modifier: Modifier = Modifier) {
    // 선택된 시술을 저장하는 상태 변수. 초기값은 null (아무것도 선택 안 됨)
    var selectedTreatment by remember { mutableStateOf<String?>(null) }
    // "예약하기" 버튼의 활성화 상태를 저장하는 상태 변수
    var isBookButtonEnabled by remember { mutableStateOf(false) }

    // 가격 정보를 저장하는 상태 변수들. 초기값은 "원"으로 설정
    var currentOriginalPrice by remember { mutableStateOf("원") }
    // 할인 금액은 시술 선택과 상관없이 항상 -10,000원으로 고정
    val fixedDiscountPrice by remember { mutableStateOf("-10,000원") }
    var currentDepositPrice by remember { mutableStateOf("원") }
    var currentStorePaymentPrice by remember { mutableStateOf("원") }

    // 시술별 더미 가격 데이터 (Map 형태로 관리)
    val dummyPrices = remember {
        mapOf(
            "컷" to mapOf(
                "original" to "20,000원",
                "deposit" to "5,000원",
                "storePayment" to "5,000원"
            ),
            "펌" to mapOf(
                "original" to "50,000원",
                "deposit" to "5,000원",
                "storePayment" to "35,000원"
            ),
            "염색" to mapOf(
                "original" to "60,000원",
                "deposit" to "5,000원",
                "storePayment" to "45,000원"
            ),
            "클리닉" to mapOf(
                "original" to "20,000원",
                "deposit" to "5,000원",
                "storePayment" to "5,000원"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // "헤어딜" 텍스트 (화면 최상단)
        Text(
            text = "헤어딜",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )

        // 1. 헤어샵 이미지 섹션
        ShopImage() // 이미지만 표시하는 Composable로 변경

        // 모든 정보 카드 부분 (시술 선택, 결제 금액)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            // "K13 헤어점" 텍스트 (카드 상단)
            Text(
                text = "K13 헤어점",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp), // 하단 패딩
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                    fontFamily = NotoSansKr
                )
            )

            // 2. 시술 종류 선택 섹션
            TreatmentSelection(
                selectedTreatment = selectedTreatment,
                onTreatmentSelected = { treatment ->
                    selectedTreatment = treatment
                    isBookButtonEnabled = true // 시술 선택 시 "예약하기" 버튼 활성화
                    // 선택된 시술에 따라 가격 정보 업데이트
                    dummyPrices[treatment]?.let { prices ->
                        currentOriginalPrice = prices["original"] ?: "원"
                        currentDepositPrice = prices["deposit"] ?: "원"
                        currentStorePaymentPrice = prices["storePayment"] ?: "원"
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. 결제 금액 정보 섹션
            PriceSummary(
                originalPrice = currentOriginalPrice,
                discountPrice = fixedDiscountPrice, // 고정된 할인 금액 전달
                depositPrice = currentDepositPrice,
                storePaymentPrice = currentStorePaymentPrice
            )
        }

        // 4. "예약하기" 버튼 섹션 (하단 고정)
        BookButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            isEnabled = isBookButtonEnabled
        )
    }
}

// 헤어샵 이미지만 표시하는 Composable 함수
@Composable
fun ShopImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.hair_shop_image),
        contentDescription = "헤어샵 이미지",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun TreatmentSelection(
    selectedTreatment: String?,
    onTreatmentSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // "시술 종류를 선택해주세요" 텍스트
        Text(
            text = "시술 종류를 선택해주세요",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(bottom = 16.dp),
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TreatmentButton(
                text = "컷",
                isSelected = selectedTreatment == "컷",
                onClick = { onTreatmentSelected("컷") }
            )
            TreatmentButton(
                text = "펌",
                isSelected = selectedTreatment == "펌",
                onClick = { onTreatmentSelected("펌") }
            )
            TreatmentButton(
                text = "염색",
                isSelected = selectedTreatment == "염색",
                onClick = { onTreatmentSelected("염색") }
            )
            TreatmentButton(
                text = "클리닉",
                isSelected = selectedTreatment == "클리닉",
                onClick = { onTreatmentSelected("클리닉") }
            )
        }
    }
}

// 개별 시술 버튼 Composable (선택 상태에 따라 색상 변경)
@Composable
fun TreatmentButton(
    text: String,
    isSelected: Boolean, // 선택 여부
    onClick: () -> Unit, // 클릭 이벤트
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color.Black else Color.White // 선택 시 검은색, 아니면 흰색
    val textColor = if (isSelected) Color.White else Color.Black // 선택 시 흰색, 아니면 검은색
    val borderColor = if (isSelected) Color.White else Color.Black // 선택 시 흰색 테두리, 아니면 검은색

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp), // 버튼 내부 패딩 조정
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(4.dp)) // 테두리 추가
            .height(40.dp) // 버튼 높이
    ) {
        Text(
            text = text,
            color = textColor, // 텍스트 색상 변경
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )
    }
}



@Composable
fun PriceSummary(
    modifier: Modifier = Modifier,
    originalPrice: String,
    discountPrice: String,
    depositPrice: String,
    storePaymentPrice: String
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // "금액" 라벨
        PriceRow(label = "금액", amount = originalPrice)
        Spacer(modifier = Modifier.height(10.dp))

        // "할인 받은 금액" 라벨
        PriceRow(label = "할인 받은 금액", amount = discountPrice)
        Spacer(modifier = Modifier.height(10.dp))

        // 구분선
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        // "헤어딜에서 결제할 예약금" 라벨
        PriceRow(label = "헤어딜에서 결제할 예약금", amount = depositPrice)
        Spacer(modifier = Modifier.height(10.dp))

        // "매장에서 결제할 금액" 라벨
        PriceRow(label = "매장에서 결제할 금액", amount = storePaymentPrice)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

// 금액 정보를 한 줄로 표시하는 보조 Composable 함수
@Composable
fun PriceRow(label: String, amount: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )
        Text(
            text = amount,
            color = Color.Black,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )
    }
}

// 4. "예약하기" 버튼 Composable 함수 (활성화 상태에 따라 색상 변경)
@Composable
fun BookButton(modifier: Modifier = Modifier, isEnabled: Boolean) { // isEnabled 파라미터 추가
    val context = LocalContext.current

    Button(
        onClick = {
            Toast.makeText(context, "예약이 완료되었습니다!", Toast.LENGTH_SHORT).show()
        },
        enabled = isEnabled, // 버튼 활성화 상태 적용
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isEnabled) Color.Black else Color.LightGray // 활성화 시 검은색, 비활성화 시 회색
        ),
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier.height(56.dp)
    ) {
        Text(
            text = "예약하기",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(
                platformStyle = PlatformTextStyle(includeFontPadding = false),
                fontFamily = NotoSansKr
            )
        )
    }
}

// 미리보기 설정
@Preview(showBackground = true, device = "id:pixel_2", name = "HairDeal Screen Preview")
@Composable
fun HairDealScreenPreview() {
    HairDealTheme {
        HairDealScreen()
    }
}

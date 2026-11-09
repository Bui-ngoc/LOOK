package com.example.data

import com.example.model.AIMetadata
import com.example.model.BlockchainMetadata
import com.example.model.LookMoment
import com.example.model.MemoryChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

object LookRepository {
  private val sampleMoments = mutableListOf(
    LookMoment(
      id = "moment-1",
      senderName = "Hà My",
      senderAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
      senderAddress = "0x89b...34C1",
      imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=800&auto=format&fit=crop&q=80",
      timeAgo = "10 phút trước",
      aiMetadata = AIMetadata(
        caption = "Cà phê sáng cùng cún cưng, năng lượng tích cực ngập tràn ☕🐶",
        mood = "Vui vẻ & Thư giãn",
        tags = listOf("#MorningCoffee", "#PuppyLove", "#WeekendVibes"),
        memoryPrompt = "Hà My vừa gửi khoảnh khắc cà phê sáng!"
      ),
      blockchainMetadata = BlockchainMetadata(
        txHash = "0x4a7e93b12389fed76412ab34cd6712398510ba45c1102983748239014ab52378",
        ipfsCid = "QmZ4tDuvesekSs4qM5ZBKpXiZGun7S2CYtEZRB3DYXkjGx",
        tokenId = "#0842",
        mintedAt = "Hôm nay, 08:30"
      ),
      reactionsCount = 14,
      isLiked = true
    ),
    LookMoment(
      id = "moment-2",
      senderName = "Minh Quân",
      senderAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
      senderAddress = "0x34A...E992",
      imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80",
      timeAgo = "1 giờ trước",
      aiMetadata = AIMetadata(
        caption = "Hoàng hôn Hồ Tây chiều nay êm đềm như một bức tranh thuỷ mặc 🌅",
        mood = "Thơ mộng",
        tags = listOf("#HoTaySunset", "#HanoiVibes", "#Peaceful"),
        memoryPrompt = "Đúng 1 năm trước bạn và Quân cũng check-in hoàng hôn ở đây!"
      ),
      blockchainMetadata = null,
      reactionsCount = 28,
      isLiked = false
    ),
    LookMoment(
      id = "moment-3",
      senderName = "Bảo Trâm",
      senderAvatar = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
      senderAddress = "0x789...12FA",
      imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&auto=format&fit=crop&q=80",
      timeAgo = "3 giờ trước",
      aiMetadata = AIMetadata(
        caption = "Deadline ngập đầu nhưng đồ ăn ngon vẫn phải ưu tiên số 1 🍕🔥",
        mood = "Hài hước",
        tags = listOf("#PizzaTime", "#CheatDay", "#WorkHardPlayHard")
      ),
      blockchainMetadata = BlockchainMetadata(
        txHash = "0x981240fbc9219374618237901a8b273461829037461298471928374612983471",
        ipfsCid = "QmXoypizjW3WknFiJnKLwHCnL72vedxjQkDDP1mXWo6uco",
        tokenId = "#0839",
        mintedAt = "Hôm qua, 19:45"
      ),
      reactionsCount = 42,
      isLiked = true
    )
  )

  private val _moments = MutableStateFlow<List<LookMoment>>(sampleMoments)
  val moments: StateFlow<List<LookMoment>> = _moments.asStateFlow()

  // Live widget current active moment
  private val _activeWidgetMoment = MutableStateFlow(sampleMoments.first())
  val activeWidgetMoment: StateFlow<LookMoment> = _activeWidgetMoment.asStateFlow()

  // Web3 state
  private val _walletAddress = MutableStateFlow("0x71C84013627A529e0630CE6Ab4828E02f1dE4E89")
  val walletAddress: StateFlow<String> = _walletAddress.asStateFlow()

  private val _isWalletConnected = MutableStateFlow(true)
  val isWalletConnected: StateFlow<Boolean> = _isWalletConnected.asStateFlow()

  // Memory chat messages
  private val _chatMessages = MutableStateFlow(
    listOf(
      MemoryChatMessage(
        id = "1",
        isUser = false,
        text = "Xin chào! Mình là Look AI Companion 🤖. Hôm nay bạn muốn tìm lại kỷ niệm nào với hội bạn thân?",
        timestamp = "09:00"
      ),
      MemoryChatMessage(
        id = "2",
        isUser = false,
        text = "💡 Nhắc bạn: Tròn 1 năm trước, bạn và Minh Quân đã chụp bức ảnh hoàng hôn tại Hồ Tây. Bạn có muốn gửi một ảnh mới lên Widget của Quân để ôn lại kỷ niệm không?",
        timestamp = "09:01",
        relatedMomentImage = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&auto=format&fit=crop&q=80"
      )
    )
  )
  val chatMessages: StateFlow<List<MemoryChatMessage>> = _chatMessages.asStateFlow()

  fun toggleLike(id: String) {
    _moments.value = _moments.value.map {
      if (it.id == id) {
        val newLiked = !it.isLiked
        it.copy(
          isLiked = newLiked,
          reactionsCount = if (newLiked) it.reactionsCount + 1 else it.reactionsCount - 1
        )
      } else it
    }
  }

  fun setWidgetMoment(moment: LookMoment) {
    _activeWidgetMoment.value = moment
  }

  fun addNewMoment(
    imageUrl: String,
    caption: String,
    mood: String,
    tags: List<String>,
    mintSbt: Boolean
  ): LookMoment {
    val newId = "moment-${UUID.randomUUID()}"
    val blockchainData = if (mintSbt) {
      BlockchainMetadata(
        txHash = "0x" + UUID.randomUUID().toString().replace("-", "") + "e9a4",
        ipfsCid = "Qm" + UUID.randomUUID().toString().replace("-", "").take(44),
        tokenId = "#" + (1000..9999).random(),
        mintedAt = "Vừa xong"
      )
    } else null

    val newMoment = LookMoment(
      id = newId,
      senderName = "Bạn (Tôi)",
      senderAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
      senderAddress = _walletAddress.value,
      imageUrl = imageUrl,
      timeAgo = "Vừa xong",
      aiMetadata = AIMetadata(
        caption = caption,
        mood = mood,
        tags = tags
      ),
      blockchainMetadata = blockchainData,
      reactionsCount = 1,
      isLiked = true
    )

    val updatedList = listOf(newMoment) + _moments.value
    _moments.value = updatedList
    _activeWidgetMoment.value = newMoment
    return newMoment
  }

  fun mintExistingMoment(momentId: String) {
    _moments.value = _moments.value.map {
      if (it.id == momentId && it.blockchainMetadata == null) {
        val minted = it.copy(
          blockchainMetadata = BlockchainMetadata(
            txHash = "0x" + UUID.randomUUID().toString().replace("-", "") + "c4b1",
            ipfsCid = "Qm" + UUID.randomUUID().toString().replace("-", "").take(44),
            tokenId = "#" + (1000..9999).random(),
            mintedAt = "Vừa xong"
          )
        )
        if (_activeWidgetMoment.value.id == momentId) {
          _activeWidgetMoment.value = minted
        }
        minted
      } else it
    }
  }

  fun sendChatMessage(userText: String) {
    val userMsg = MemoryChatMessage(
      id = UUID.randomUUID().toString(),
      isUser = true,
      text = userText,
      timestamp = "Vừa xong"
    )
    _chatMessages.value = _chatMessages.value + userMsg

    // Generate responsive AI response
    val aiResponseText = when {
      userText.contains("kỷ niệm", ignoreCase = true) || userText.contains("hồ tây", ignoreCase = true) ->
        "Kỷ niệm Hồ Tây đó của bạn và Quân nhận được 28 lượt thả tim trên Widget đấy! Mình gợi ý bạn chụp ngay một ảnh selfie kèm caption: 'Hồ Tây vẫn thế, chỉ có tình bạn chúng mình là nhân đôi!' 🚀"
      userText.contains("nft", ignoreCase = true) || userText.contains("sbt", ignoreCase = true) || userText.contains("web3", ignoreCase = true) ->
        "Bạn hiện có 2 khoảnh khắc đã được đúc thành Soulbound Token (SBT) vĩnh cửu trên Polygon. Chúng được mã hóa IPFS an toàn tuyệt đối và không thể bị sửa đổi."
      userText.contains("caption", ignoreCase = true) ->
        "Gợi ý caption hot trend hôm nay: 'Mỗi khoảnh khắc xuất hiện trên widget của cậu là một lời nhắc tớ luôn ở đây 💛'. Bạn thử dùng nhé!"
      else ->
        "Look AI đã ghi nhận! Bạn có thể chọn bất kỳ ảnh nào để mình sinh caption thơ/hài hước hoặc chọn khoảnh khắc đẹp nhất để đúc thành kỷ vật Web3 nhé ✨."
    }

    val aiMsg = MemoryChatMessage(
      id = UUID.randomUUID().toString(),
      isUser = false,
      text = aiResponseText,
      timestamp = "Vừa xong"
    )
    _chatMessages.value = _chatMessages.value + aiMsg
  }
}

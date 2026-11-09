package com.example.model

enum class AudienceVisibility {
  PUBLIC,
  CLOSE_FRIENDS,
  SPECIFIC
}

enum class MediaType {
  PHOTO,
  VIDEO
}

data class AIMetadata(
  val caption: String,
  val mood: String,
  val tags: List<String>,
  val memoryPrompt: String? = null
)

data class BlockchainMetadata(
  val txHash: String,
  val ipfsCid: String,
  val chain: String = "Polygon Mainnet",
  val contractAddress: String = "0x89205A3A3b2A69De6Dbf7f01ED13B2108B2c43e7",
  val tokenId: String,
  val isSoulbound: Boolean = true,
  val mintedAt: String
)

data class LookMoment(
  val id: String,
  val senderName: String,
  val senderAvatar: String,
  val senderAddress: String? = null,
  val imageUrl: String,
  val timeAgo: String,
  val aiMetadata: AIMetadata,
  val blockchainMetadata: BlockchainMetadata? = null,
  val reactionsCount: Int = 0,
  val isLiked: Boolean = false,
  val mediaType: MediaType = MediaType.PHOTO,
  val audience: AudienceVisibility = AudienceVisibility.CLOSE_FRIENDS,
  val taggedFriends: List<String> = emptyList()
)

data class LookThought(
  val id: String,
  val authorName: String,
  val authorAvatar: String,
  val text: String,
  val bgGradient: List<String>,
  val emoji: String,
  val timeAgo: String
)

data class MemoryChatMessage(
  val id: String,
  val isUser: Boolean,
  val text: String,
  val timestamp: String,
  val relatedMomentImage: String? = null
)

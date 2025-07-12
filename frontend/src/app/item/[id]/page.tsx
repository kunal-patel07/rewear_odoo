"use client"

import { useState } from "react"
import { ArrowLeft, Heart, Share2, Star, MessageCircle, Shield, Truck } from "lucide-react"
import Image from "next/image"
import Link from "next/link"

// Mock data - in real app this would come from API
const getItemById = (id: string) => {
  const items = [
    {
      id: "1",
      title: "Vintage Denim Jacket",
      price: 45,
      originalPrice: 120,
      images: [
        "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=600&h=800&fit=crop",
        "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=600&h=800&fit=crop&crop=top",
        "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=600&h=800&fit=crop&crop=bottom",
      ],
      condition: "Like New",
      brand: "Levi's",
      size: "Medium",
      color: "Blue",
      material: "100% Cotton Denim",
      description:
        "Classic vintage Levi's denim jacket in excellent condition. This timeless piece features the iconic Levi's styling with button closure, chest pockets, and a comfortable fit. Perfect for layering and adding a vintage touch to any outfit.",
      seller: {
        name: "Sarah Johnson",
        username: "@sarah_vintage",
        avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face",
        rating: 4.8,
        totalSales: 24,
        joinDate: "March 2023",
        verified: true,
      },
      category: "Women's Outerwear",
      postedDate: "2024-01-15",
      views: 156,
      likes: 23,
      location: "New York, NY",
    },
  ]
  return items.find((item) => item.id === id)
}

export default function ItemDetailPage({ params }: { params: { id: string } }) {
  const [selectedImageIndex, setSelectedImageIndex] = useState(0)
  const [showSwapModal, setShowSwapModal] = useState(false)
  const item = getItemById(params.id)

  if (!item) {
    return <div>Item not found</div>
  }

  const previousListings = [
    {
      id: 1,
      image: "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=300&h=400&fit=crop",
      title: "Wool Sweater",
      price: "$28",
    },
    {
      id: 2,
      image: "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=300&h=400&fit=crop",
      title: "Summer Dress",
      price: "$35",
    },
    {
      id: 3,
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=300&h=400&fit=crop",
      title: "Designer Handbag",
      price: "$85",
    },
    {
      id: 4,
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=400&fit=crop",
      title: "Running Sneakers",
      price: "$35",
    },
  ]

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <Link href="/browse" className="flex-shrink-0 flex items-center mr-4">
                <ArrowLeft className="w-5 h-5 mr-2" />
                Back to Browse
              </Link>
              <Link href="/" className="flex-shrink-0 flex items-center">
                <Image src="/images/rewear-logo.svg" alt="Rewear Logo" width={120} height={40} className="h-8 w-auto" />
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Search Bar */}
      <div className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="relative">
            <input
              type="text"
              placeholder="Search for similar items..."
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 pr-10"
            />
            <button className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="bg-white rounded-xl shadow-lg overflow-hidden">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 p-8">
            {/* Left Column - Images */}
            <div>
              <div className="space-y-4">
                {/* Main Image */}
                <div className="relative aspect-square rounded-lg overflow-hidden bg-gray-100">
                  <Image
                    src={item.images[selectedImageIndex] || "/placeholder.svg"}
                    alt={item.title}
                    fill
                    className="object-cover"
                  />
                  <button className="absolute top-4 right-4 bg-white rounded-full p-2 hover:bg-gray-100 transition-colors shadow-md">
                    <Heart className="w-5 h-5 text-gray-600" />
                  </button>
                  <button className="absolute top-4 left-4 bg-white rounded-full p-2 hover:bg-gray-100 transition-colors shadow-md">
                    <Share2 className="w-5 h-5 text-gray-600" />
                  </button>
                </div>

                {/* Thumbnail Images */}
                <div className="grid grid-cols-4 gap-2">
                  {item.images.map((image, index) => (
                    <button
                      key={index}
                      onClick={() => setSelectedImageIndex(index)}
                      className={`relative aspect-square rounded-lg overflow-hidden border-2 transition-colors ${
                        selectedImageIndex === index ? "border-green-500" : "border-gray-200 hover:border-gray-300"
                      }`}
                    >
                      <Image
                        src={image || "/placeholder.svg"}
                        alt={`${item.title} ${index + 1}`}
                        fill
                        className="object-cover"
                      />
                    </button>
                  ))}
                </div>
              </div>
            </div>

            {/* Right Column - Product Details */}
            <div className="space-y-6">
              {/* Title and Price */}
              <div>
                <div className="flex items-center gap-2 mb-2">
                  <span className="bg-green-100 text-green-800 px-2 py-1 rounded text-sm font-semibold">
                    {item.condition}
                  </span>
                  <span className="text-gray-500 text-sm">{item.category}</span>
                </div>
                <h1 className="text-3xl font-bold text-gray-900 mb-2">{item.title}</h1>
                <p className="text-gray-600 mb-4">{item.brand}</p>
                <div className="flex items-center gap-4 mb-4">
                  <span className="text-3xl font-bold text-green-600">${item.price}</span>
                  <span className="text-xl text-gray-500 line-through">${item.originalPrice}</span>
                  <span className="bg-red-100 text-red-800 px-2 py-1 rounded text-sm font-semibold">
                    {Math.round(((item.originalPrice - item.price) / item.originalPrice) * 100)}% OFF
                  </span>
                </div>
              </div>

              {/* Product Details */}
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <span className="text-sm font-medium text-gray-700">Size:</span>
                    <p className="text-gray-900">{item.size}</p>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-700">Color:</span>
                    <p className="text-gray-900">{item.color}</p>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-700">Material:</span>
                    <p className="text-gray-900">{item.material}</p>
                  </div>
                  <div>
                    <span className="text-sm font-medium text-gray-700">Location:</span>
                    <p className="text-gray-900">{item.location}</p>
                  </div>
                </div>
              </div>

              {/* Description */}
              <div>
                <h3 className="text-lg font-semibold text-gray-900 mb-3">Description</h3>
                <div className="bg-gray-50 rounded-lg p-4">
                  <p className="text-gray-700 leading-relaxed">{item.description}</p>
                </div>
              </div>

              {/* Seller Info */}
              <div className="border-t pt-6">
                <h3 className="text-lg font-semibold text-gray-900 mb-3">Seller Information</h3>
                <div className="flex items-center space-x-4">
                  <Image
                    src={item.seller.avatar || "/placeholder.svg"}
                    alt={item.seller.name}
                    width={60}
                    height={60}
                    className="rounded-full"
                  />
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <h4 className="font-semibold text-gray-900">{item.seller.name}</h4>
                      {item.seller.verified && <Shield className="w-4 h-4 text-green-600"  />}
                    </div>
                    <p className="text-gray-600">@{item.seller.username}</p>
                    <div className="flex items-center gap-4 mt-1">
                      <div className="flex items-center">
                        <Star className="w-4 h-4 text-yellow-400 fill-current mr-1" />
                        <span className="text-sm text-gray-600">
                          {item.seller.rating} ({item.seller.totalSales} sales)
                        </span>
                      </div>
                      <span className="text-sm text-gray-500">Joined {item.seller.joinDate}</span>
                    </div>
                  </div>
                </div>
              </div>

              {/* Action Buttons */}
              <div className="space-y-3 pt-6 border-t">
                <button
                  onClick={() => setShowSwapModal(true)}
                  className="w-full bg-green-600 hover:bg-green-700 text-white py-4 px-6 rounded-lg font-semibold text-lg transition-colors"
                >
                  Propose Swap
                </button>
                <div className="grid grid-cols-2 gap-3">
                  <button className="flex items-center justify-center gap-2 border border-gray-300 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-50 transition-colors">
                    <MessageCircle className="w-5 h-5" />
                    Message Seller
                  </button>
                  <button className="flex items-center justify-center gap-2 border border-gray-300 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-50 transition-colors">
                    <Truck className="w-5 h-5" />
                    Buy Now
                  </button>
                </div>
              </div>

              {/* Stats */}
              <div className="flex items-center justify-between text-sm text-gray-500 pt-4 border-t">
                <span>{item.views} views</span>
                <span>{item.likes} likes</span>
                <span>Posted {new Date(item.postedDate).toLocaleDateString()}</span>
              </div>
            </div>
          </div>

          {/* Previous Listings Section */}
          <div className="border-t bg-gray-50 p-8">
            <h3 className="text-xl font-semibold text-gray-900 mb-6">More from {item.seller.name}</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {previousListings.map((listing) => (
                <Link
                  key={listing.id}
                  href={`/item/${listing.id}`}
                  className="bg-white rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow"
                >
                  <div className="aspect-square relative">
                    <Image
                      src={listing.image || "/placeholder.svg"}
                      alt={listing.title}
                      fill
                      className="object-cover"
                    />
                  </div>
                  <div className="p-3">
                    <h4 className="font-medium text-gray-900 text-sm mb-1">{listing.title}</h4>
                    <p className="text-green-600 font-semibold">{listing.price}</p>
                  </div>
                </Link>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* Swap Modal */}
      {showSwapModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-md w-full p-6">
            <h3 className="text-xl font-semibold text-gray-900 mb-4">Propose a Swap</h3>
            <p className="text-gray-600 mb-6">
              Select an item from your closet to propose a swap with {item.seller.name}
            </p>
            <div className="space-y-4">
              <button className="w-full border-2 border-dashed border-gray-300 rounded-lg p-8 text-center hover:border-green-500 transition-colors">
                <div className="text-gray-400 mb-2">
                  <svg className="w-12 h-12 mx-auto" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                  </svg>
                </div>
                <p className="text-gray-600">Select item to swap</p>
              </button>
              <textarea
                placeholder="Add a message to your swap proposal..."
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 resize-none"
                rows={3}
              />
              <div className="flex gap-3">
                <button
                  onClick={() => setShowSwapModal(false)}
                  className="flex-1 border border-gray-300 text-gray-700 py-3 px-4 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  Cancel
                </button>
                <button className="flex-1 bg-green-600 hover:bg-green-700 text-white py-3 px-4 rounded-lg transition-colors">
                  Send Proposal
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

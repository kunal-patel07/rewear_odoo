"use client"
import { useState } from "react"
import { Search, Edit, Eye, Ban, CheckCircle, XCircle } from "lucide-react"
import Image from "next/image"
import Link from "next/link"

export default function AdminPanel() {
  const [activeTab, setActiveTab] = useState("users")
  const [searchQuery, setSearchQuery] = useState("")

  const users = [
    {
      id: 1,
      name: "Sarah Johnson",
      username: "sarah_style",
      email: "sarah@example.com",
      avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face",
      joinDate: "2024-01-15",
      status: "Active",
      totalListings: 12,
      totalPurchases: 8,
      rating: 4.8,
      verified: true,
    },
    {
      id: 2,
      name: "Mike Chen",
      username: "mike_sports",
      email: "mike@example.com",
      avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop&crop=face",
      joinDate: "2024-01-10",
      status: "Active",
      totalListings: 6,
      totalPurchases: 15,
      rating: 4.6,
      verified: false,
    },
    {
      id: 3,
      name: "Emma Davis",
      username: "emma_fashion",
      email: "emma@example.com",
      avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop&crop=face",
      joinDate: "2024-01-08",
      status: "Suspended",
      totalListings: 24,
      totalPurchases: 3,
      rating: 4.2,
      verified: true,
    },
    {
      id: 4,
      name: "Alex Rodriguez",
      username: "alex_vintage",
      email: "alex@example.com",
      avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face",
      joinDate: "2024-01-05",
      status: "Active",
      totalListings: 18,
      totalPurchases: 12,
      rating: 4.9,
      verified: true,
    },
  ]

  const orders = [
    {
      id: "ORD-001",
      buyer: "Sarah Johnson",
      seller: "Mike Chen",
      item: "Vintage Denim Jacket",
      amount: 45,
      status: "Completed",
      date: "2024-01-20",
      image: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=100&h=100&fit=crop",
    },
    {
      id: "ORD-002",
      buyer: "Emma Davis",
      seller: "Alex Rodriguez",
      item: "Designer Handbag",
      amount: 85,
      status: "In Transit",
      date: "2024-01-19",
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=100&h=100&fit=crop",
    },
    {
      id: "ORD-003",
      buyer: "Mike Chen",
      seller: "Sarah Johnson",
      item: "Running Sneakers",
      amount: 35,
      status: "Pending",
      date: "2024-01-18",
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=100&h=100&fit=crop",
    },
    {
      id: "ORD-004",
      buyer: "Alex Rodriguez",
      seller: "Emma Davis",
      item: "Wool Sweater",
      amount: 28,
      status: "Cancelled",
      date: "2024-01-17",
      image: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=100&h=100&fit=crop",
    },
  ]

  const listings = [
    {
      id: "LST-001",
      title: "Vintage Denim Jacket",
      seller: "Sarah Johnson",
      price: 45,
      status: "Active",
      views: 156,
      likes: 23,
      date: "2024-01-15",
      image: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=100&h=100&fit=crop",
      category: "Women's Outerwear",
    },
    {
      id: "LST-002",
      title: "Designer Handbag",
      seller: "Emma Davis",
      price: 85,
      status: "Sold",
      views: 89,
      likes: 12,
      date: "2024-01-12",
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=100&h=100&fit=crop",
      category: "Accessories",
    },
    {
      id: "LST-003",
      title: "Running Sneakers",
      seller: "Mike Chen",
      price: 35,
      status: "Under Review",
      views: 45,
      likes: 8,
      date: "2024-01-10",
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=100&h=100&fit=crop",
      category: "Men's Footwear",
    },
    {
      id: "LST-004",
      title: "Wool Sweater",
      seller: "Alex Rodriguez",
      price: 28,
      status: "Active",
      views: 67,
      likes: 15,
      date: "2024-01-08",
      image: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=100&h=100&fit=crop",
      category: "Women's Clothing",
    },
  ]

  const getStatusColor = (status: string) => {
    switch (status.toLowerCase()) {
      case "active":
      case "completed":
        return "bg-green-100 text-green-800"
      case "pending":
      case "in transit":
      case "under review":
        return "bg-yellow-100 text-yellow-800"
      case "suspended":
      case "cancelled":
        return "bg-red-100 text-red-800"
      case "sold":
        return "bg-blue-100 text-blue-800"
      default:
        return "bg-gray-100 text-gray-800"
    }
  }

  const renderUsers = () => (
    <div className="space-y-4">
      {users.map((user) => (
        <div key={user.id} className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex items-center gap-6">
            {/* Avatar */}
            <div className="flex-shrink-0">
              <Image
                src={user.avatar || "/placeholder.svg"}
                alt={user.name}
                width={80}
                height={80}
                className="rounded-full"
              />
            </div>

            {/* Details */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-3 mb-2">
                <h3 className="text-lg font-semibold text-gray-900">{user.name}</h3>
                <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusColor(user.status)}`}>
                  {user.status}
                </span>
                {user.verified && <CheckCircle className="w-5 h-5 text-green-600" />}
              </div>
              <p className="text-gray-600 mb-1">
                @{user.username} • {user.email}
              </p>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-500">
                <div>
                  <span className="font-medium">Joined:</span> {new Date(user.joinDate).toLocaleDateString()}
                </div>
                <div>
                  <span className="font-medium">Listings:</span> {user.totalListings}
                </div>
                <div>
                  <span className="font-medium">Purchases:</span> {user.totalPurchases}
                </div>
                <div>
                  <span className="font-medium">Rating:</span> ⭐ {user.rating}
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col gap-2">
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Eye className="w-4 h-4" />
                View Profile
              </button>
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Edit className="w-4 h-4" />
                Edit User
              </button>
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-red-300 text-red-600 rounded-lg hover:bg-red-50 transition-colors">
                <Ban className="w-4 h-4" />
                Suspend
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )

  const renderOrders = () => (
    <div className="space-y-4">
      {orders.map((order) => (
        <div key={order.id} className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex items-center gap-6">
            {/* Order Image */}
            <div className="flex-shrink-0">
              <Image
                src={order.image || "/placeholder.svg"}
                alt={order.item}
                width={80}
                height={80}
                className="rounded-lg object-cover"
              />
            </div>

            {/* Details */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-3 mb-2">
                <h3 className="text-lg font-semibold text-gray-900">#{order.id}</h3>
                <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusColor(order.status)}`}>
                  {order.status}
                </span>
              </div>
              <p className="text-gray-900 font-medium mb-1">{order.item}</p>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-500">
                <div>
                  <span className="font-medium">Buyer:</span> {order.buyer}
                </div>
                <div>
                  <span className="font-medium">Seller:</span> {order.seller}
                </div>
                <div>
                  <span className="font-medium">Amount:</span> ${order.amount}
                </div>
                <div>
                  <span className="font-medium">Date:</span> {new Date(order.date).toLocaleDateString()}
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col gap-2">
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Eye className="w-4 h-4" />
                View Details
              </button>
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Edit className="w-4 h-4" />
                Update Status
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )

  const renderListings = () => (
    <div className="space-y-4">
      {listings.map((listing) => (
        <div key={listing.id} className="bg-white rounded-lg shadow-sm border p-6">
          <div className="flex items-center gap-6">
            {/* Listing Image */}
            <div className="flex-shrink-0">
              <Image
                src={listing.image || "/placeholder.svg"}
                alt={listing.title}
                width={80}
                height={80}
                className="rounded-lg object-cover"
              />
            </div>

            {/* Details */}
            <div className="flex-1 min-w-0">
              <div className="flex items-center gap-3 mb-2">
                <h3 className="text-lg font-semibold text-gray-900">{listing.title}</h3>
                <span className={`px-2 py-1 rounded-full text-xs font-semibold ${getStatusColor(listing.status)}`}>
                  {listing.status}
                </span>
              </div>
              <p className="text-gray-600 mb-1">
                by {listing.seller} • {listing.category}
              </p>
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-500">
                <div>
                  <span className="font-medium">Price:</span> ${listing.price}
                </div>
                <div>
                  <span className="font-medium">Views:</span> {listing.views}
                </div>
                <div>
                  <span className="font-medium">Likes:</span> {listing.likes}
                </div>
                <div>
                  <span className="font-medium">Listed:</span> {new Date(listing.date).toLocaleDateString()}
                </div>
              </div>
            </div>

            {/* Actions */}
            <div className="flex flex-col gap-2">
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Eye className="w-4 h-4" />
                View Item
              </button>
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
                <Edit className="w-4 h-4" />
                Edit Listing
              </button>
              <button className="flex items-center gap-2 px-3 py-2 text-sm border border-red-300 text-red-600 rounded-lg hover:bg-red-50 transition-colors">
                <XCircle className="w-4 h-4" />
                Remove
              </button>
            </div>
          </div>
        </div>
      ))}
    </div>
  )

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <Link href="/" className="flex-shrink-0 flex items-center">
                <Image src="/images/rewear-logo.svg" alt="Rewear Logo" width={120} height={40} className="h-8 w-auto" />
              </Link>
              <span className="ml-4 text-gray-500">Admin Panel</span>
            </div>

            <div className="flex items-center space-x-4">
              <Link
                href="/"
                className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
              >
                Back to Site
              </Link>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Tab Navigation */}
        <div className="bg-white rounded-xl shadow-sm mb-8">
          <div className="flex border-b">
            <button
              onClick={() => setActiveTab("users")}
              className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === "users"
                  ? "border-green-500 text-green-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              Manage Users
            </button>
            <button
              onClick={() => setActiveTab("orders")}
              className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === "orders"
                  ? "border-green-500 text-green-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              Manage Orders
            </button>
            <button
              onClick={() => setActiveTab("listings")}
              className={`px-6 py-4 text-sm font-medium border-b-2 transition-colors ${
                activeTab === "listings"
                  ? "border-green-500 text-green-600"
                  : "border-transparent text-gray-500 hover:text-gray-700"
              }`}
            >
              Manage Listings
            </button>
          </div>

          {/* Search Bar */}
          <div className="p-6">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder={`Search ${activeTab}...`}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
              />
            </div>
          </div>
        </div>

        {/* Content */}
        <div>
          <div className="mb-6">
            <h2 className="text-2xl font-bold text-gray-900 capitalize">Manage {activeTab}</h2>
            <p className="text-gray-600 mt-1">
              {activeTab === "users" && "View and manage user accounts, permissions, and activity"}
              {activeTab === "orders" && "Monitor and manage all orders, transactions, and disputes"}
              {activeTab === "listings" && "Review and moderate item listings, approve or remove content"}
            </p>
          </div>

          {activeTab === "users" && renderUsers()}
          {activeTab === "orders" && renderOrders()}
          {activeTab === "listings" && renderListings()}
        </div>
      </div>
    </div>
  )
}

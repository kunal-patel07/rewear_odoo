"use client"

import type React from "react"

import { useState } from "react"
import { Eye, EyeOff, ArrowLeft, AlertCircle, CheckCircle } from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { register, type RegisterRequest, type ApiError } from "@/lib/auth"

export default function SignUpPage() {
  const [showPassword, setShowPassword] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)
  const [formData, setFormData] = useState({
    name: "",
    username: "",
    email: "",
    password: "",
    description: "",
  })

  const router = useRouter()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    setSuccess(false)

    try {
      const registerData: RegisterRequest = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
        username: formData.username,
      }

      const response = await register(registerData)
      
      if (response.data?.id) {
        // Registration successful
        setSuccess(true)
        // Redirect to login page after a short delay
        setTimeout(() => {
          router.push('/login?message=Registration successful! Please sign in.')
        }, 2000)
      } else {
        setError('Registration failed. Please try again.')
      }
    } catch (err) {
      console.error('Registration error:', err)
      if (err instanceof Error) {
        setError(err.message)
      } else {
        setError('An unexpected error occurred. Please try again.')
      }
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
    // Clear error when user starts typing
    if (error) {
      setError(null)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-green-50 to-blue-50 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8">
        {/* Header */}
        <div className="text-center">
          <Link
            href="/"
            className="inline-flex items-center text-green-600 hover:text-green-700 mb-6 transition-colors"
          >
            <ArrowLeft className="w-4 h-4 mr-2" />
            Back to Home
          </Link>
          <div className="flex justify-center mb-6">
            <Image src="/images/rewear-logo.svg" alt="Rewear Logo" width={150} height={50} className="h-12 w-auto" />
          </div>
          <h2 className="text-3xl font-bold text-gray-900">Join Rewear</h2>
          <p className="mt-2 text-gray-600">Create your account and start your sustainable fashion journey</p>
        </div>

        {/* Signup Form */}
        <div className="bg-white rounded-xl shadow-lg p-8">
          {error && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-center">
              <AlertCircle className="w-5 h-5 text-red-500 mr-3 flex-shrink-0" />
              <p className="text-red-700 text-sm">{error}</p>
            </div>
          )}

          {success && (
            <div className="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg flex items-center">
              <CheckCircle className="w-5 h-5 text-green-500 mr-3 flex-shrink-0" />
              <p className="text-green-700 text-sm">
                Registration successful! Redirecting to login page...
              </p>
            </div>
          )}

          <form className="space-y-6" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">
                Full Name
              </label>
              <input
                id="name"
                name="name"
                type="text"
                required
                value={formData.name}
                onChange={handleChange}
                disabled={loading || success}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                placeholder="Enter your full name"
              />
            </div>

            <div>
              <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
                Username
              </label>
              <input
                id="username"
                name="username"
                type="text"
                required
                value={formData.username}
                onChange={handleChange}
                disabled={loading || success}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                placeholder="Choose a username"
              />
            </div>

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                Email Address
              </label>
              <input
                id="email"
                name="email"
                type="email"
                required
                value={formData.email}
                onChange={handleChange}
                disabled={loading || success}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                placeholder="Enter your email"
              />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                Password
              </label>
              <div className="relative">
                <input
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  required
                  value={formData.password}
                  onChange={handleChange}
                  disabled={loading || success}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 transition-colors pr-12 disabled:opacity-50 disabled:cursor-not-allowed"
                  placeholder="Create a password"
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  disabled={loading || success}
                  className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600 disabled:opacity-50"
                >
                  {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
              </div>
              <p className="mt-1 text-xs text-gray-500">Password must be at least 8 characters long</p>
            </div>

            <div>
              <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">
                About You <span className="text-gray-400">(Optional)</span>
              </label>
              <textarea
                id="description"
                name="description"
                rows={3}
                value={formData.description}
                onChange={handleChange}
                disabled={loading || success}
                className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 transition-colors resize-none disabled:opacity-50 disabled:cursor-not-allowed"
                placeholder="Tell us a bit about yourself and your style preferences..."
              />
            </div>

            <div className="flex items-center">
              <input
                id="terms"
                name="terms"
                type="checkbox"
                required
                disabled={loading || success}
                className="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded disabled:opacity-50"
              />
              <label htmlFor="terms" className="ml-2 block text-sm text-gray-700">
                I agree to the{" "}
                <a href="#" className="text-green-600 hover:text-green-700 transition-colors">
                  Terms of Service
                </a>{" "}
                and{" "}
                <a href="#" className="text-green-600 hover:text-green-700 transition-colors">
                  Privacy Policy
                </a>
              </label>
            </div>

            <button
              type="submit"
              disabled={loading || success}
              className="w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-4 rounded-lg transition-colors duration-200 transform hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
            >
              {loading ? (
                <div className="flex items-center justify-center">
                  <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                  Creating Account...
                </div>
              ) : success ? (
                <div className="flex items-center justify-center">
                  <CheckCircle className="w-5 h-5 mr-2" />
                  Account Created!
                </div>
              ) : (
                'Create Account'
              )}
            </button>
          </form>

          <p className="mt-6 text-center text-sm text-gray-600">
            Already have an account?{" "}
            <Link href="/login" className="text-green-600 hover:text-green-700 font-medium transition-colors">
              Sign in here
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}

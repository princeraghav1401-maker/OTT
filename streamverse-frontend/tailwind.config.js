/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx}",
  ],
  theme: {
    extend: {
      colors: {
        dark: "#0b0f19",
        card: "#141a2a",
        primary: "#ff6b35",
        primaryDark: "#e85a2b",
      },
      fontFamily: {
        poppins: ["Poppins", "sans-serif"],
      },
      boxShadow: {
        glow: "0 0 30px rgba(255,107,53,0.35)",
      },
    },
  },
  plugins: [],
};
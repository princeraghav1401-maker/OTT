import { useEffect, useState } from "react";
import { Users } from "lucide-react";
import api from "../../api/api";

const AdminUserManagementPage = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    api
      .get("/admin/users")
      .then((res) => setUsers(Array.isArray(res.data) ? res.data : []))
      .catch(() => setUsers([]));
  }, []);

  return (
    <div className="min-h-screen bg-dark p-8 text-white">
      <h1 className="text-4xl font-black">Manage Users</h1>
      <p className="mt-2 text-gray-400">Registered users on StreamVerse.</p>

      {users.length === 0 ? (
        <div className="mt-8 rounded-3xl border border-white/10 bg-white/5 p-16 text-center">
          <Users className="mx-auto mb-4 text-primary" size={55} />
          <h2 className="text-2xl font-black">No users endpoint/data found</h2>
          <p className="mt-2 text-gray-400">
            Backend me /api/admin/users endpoint connect karna hoga.
          </p>
        </div>
      ) : (
        <div className="mt-8 overflow-hidden rounded-3xl border border-white/10 bg-white/5">
          <table className="w-full">
            <thead className="bg-black/30 text-left">
              <tr>
                <th className="p-4">Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {users.map((user) => (
                <tr key={user.id} className="border-t border-white/10">
                  <td className="p-4">{user.name}</td>
                  <td>{user.email}</td>
                  <td>{user.role || "USER"}</td>
                  <td>{user.active ? "Active" : "Inactive"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default AdminUserManagementPage;
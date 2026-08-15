export interface UserProfile {
  email: string;
  username: string;
  subscriptions: { id: number; title: string }[];
}

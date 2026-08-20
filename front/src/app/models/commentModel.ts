export interface CommentModel {
  id: number;
  content: string;
  author: {
    id: number;
    username: string;
  };
  createdAt: string;
}

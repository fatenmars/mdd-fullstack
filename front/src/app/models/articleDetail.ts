import { Comment } from './comment';

export interface ArticleDetail {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  author: {
    id: number;
    username: string;
  };
  theme: {
    id: number;
    title: string;
  };
  comments: Comment[];
}

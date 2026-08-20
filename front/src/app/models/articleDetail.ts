import { CommentModel } from './commentModel';

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
  comments: CommentModel[];
}

package com.luoExpress._00_leetcode.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * https://leetcode.com/problems/invert-binary-tree/
 * @author Dal
 *
 */
public class _226_ReverseBinaryTree {//将所有节点的左右子树都交换
	
//   public TreeNode invertTree(TreeNode root) {preorder 在递归调用中，root就是每次递归调用中的新的当前节点
//	   if (root == null) return root;
//	   
//	   TreeNode tmp = root.left;
//	   root.left = root.right;
//	   root.right = tmp;
//	   
//       invertTree(root.left);
//       invertTree(root.right);
//       
//       return root;
//   }
	
//	public TreeNode invertTree(TreeNode root) {//postorder
//	   if (root == null) return root;
//	   
//       invertTree(root.left);
//       invertTree(root.right);
//	   
//	   TreeNode tmp = root.left;
//	   root.left = root.right;
//	   root.right = tmp;
//       
//       return root;
//    }
	
//	public TreeNode invertTree(TreeNode root) {//inorder  在递归调用中，root就是每次递归调用中的新的当前节点
//	   if (root == null) return root;
//	   
//       invertTree(root.left);
//
//	   TreeNode tmp = root.left;
//	   root.left = root.right;
//	   root.right = tmp;
//
//       invertTree(root.left);//这里的left相当于是没有交换前的right
//       
//       return root;
//    }
	public TreeNode invertTree(TreeNode root) {
		if (root == null) return root;
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
		    TreeNode tmp = node.left;
		    node.left = node.right;
		    node.right = tmp;
			
			if (node.left != null) {
				queue.offer(node.left);
			}
			
			if (node.right != null) {
				queue.offer(node.right);
			}
		}
		return root;
	}
}

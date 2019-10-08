package com.luoExpress._00_leetcode.list;

/**
 * https://leetcode.com/problems/delete-node-in-a-linked-list/
 * @author Dal
 *
 */
public class _237_deleteListNode {
	
	public void deleteNode(ListNode node) {
		//做法：覆盖的方式
		node.val = node.next.val;
		node.next = node.next.next;
    }
}

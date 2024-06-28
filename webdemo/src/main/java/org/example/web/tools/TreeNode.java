package org.example.web.tools;


import java.util.ArrayList;
import java.util.List;

public class TreeNode {
  private String value;
  private String label;
  private List<TreeNode> children;

  public TreeNode(String value, String label) {
    this.value = value;
    this.label = label;
    this.children = new ArrayList<>();
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<TreeNode> getChildren() {
    return children;
  }

  public void setChildren(List<TreeNode> children) {
    this.children = children;
  }

  @Override
  public String toString() {
    return toString(0);
  }

  private String toString(int indent) {
    StringBuilder sb = new StringBuilder();
    String indentStr = " ".repeat(indent * 2);
    sb.append(indentStr).append("{\n");
    sb.append(indentStr).append("  \"value\": \"").append(value).append("\",\n");
    sb.append(indentStr).append("  \"label\": \"").append(label).append("\",\n");
    sb.append(indentStr).append("  \"children\": [\n");

    for (TreeNode child : children) {
      sb.append(child.toString(indent + 2)).append(",\n");
    }

    if (!children.isEmpty()) {
      sb.setLength(sb.length() - 2);  // Remove the last comma and newline
      sb.append("\n");
    }

    sb.append(indentStr).append("  ]\n");
    sb.append(indentStr).append("}");
    return sb.toString();
  }
}


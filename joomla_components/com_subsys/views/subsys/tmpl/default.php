<?php defined('_JEXEC') or die('Restricted access'); ?>
<form action="index.php" method="post" name="adminForm">
<div id="editcell">
	<table class="adminlist">
	<thead>
		<tr>
			<th width="5">
				<?php echo JText::_( 'ID' ); ?>
			</th>
			<th width="20">
				<input type="checkbox" name="toggle" value="" onclick="checkAll(<?php echo count( $this->items ); ?>);" />
			</th>			
			<th>
				<?php echo JText::_( 'Subscriber_Code' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscriber_Name' ); ?>
			</th>
			<th>
				<?php echo JText::_( 'Subscriber_Address' ); ?>
			</th>
		</tr>
	</thead>
	<?php
	$k = 0;
	for ($i=0, $n=count( $this->items ); $i < $n; $i++)	{
		$row =& $this->items[$i];
		$checked 	= JHTML::_('grid.id',   $i, $row->sub_id );
		$link 		= JRoute::_( 'index.php?option=com_subsys&controller=subscriber&task=edit&cid[]='. $row->sub_id );		
		?>
		<tr class="<?php echo "row$k"; ?>">
			<td>
				<?php echo $row->sub_id; ?>
			</td>
			<td>
				<?php echo $checked; ?>
			</td>
			<td>
				<?php echo $row->sub_code; ?>
			</td>
			<td>
				<a href="<?php echo $link; ?>"><?php echo $row->sub_name; ?></a>
			</td>
			<td>
				<?php echo $row->sub_address; ?>
			</td>
		</tr>
		<?php
		$k = 1 - $k;
	}
	?>
	<tfoot>
    <tr>
      <td colspan="9"><?php echo $this->pagination->getListFooter(); ?></td>
    </tr>
  </tfoot>
	</table>
</div>

<input type="hidden" name="option" value="com_subsys" />
<input type="hidden" name="task" value="" />
<input type="hidden" name="boxchecked" value="0" />
<input type="hidden" name="controller" value="subscriber" />
<input type="hidden" name="view" value="subsys" />
</form>
